package tpgrails

import grails.validation.ValidationException
import org.apache.commons.lang.RandomStringUtils

import static org.springframework.http.HttpStatus.*

class UserController {

    UserService userService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond userService.list(params), model:[userCount: userService.count()]
    }

    def show(Long id) {
        respond userService.get(id)
    }

    def create() {
        respond new User(params)
    }

    def save(User user) {
        if (user == null) {
            notFound()
            return
        }

        def file = request.getFile("filename")
        String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), file);
        name+=".jpg"
        try{
            if(file && !file.empty){
                file.transferTo(new File(grailsApplication.config.maconfig.assets_path+"${name}"))
                flash.message="your.sucessful.file.upload.message"
            }
            else{
                flash.message="your.unsucessful.file.upload.message"
            }
        }
        catch(Exception e){
            log.error("Your exception message goes here",e)
        }

        user.thumbnail=new Illustration(filename: name)



        try {
            userService.save(user)
        } catch (ValidationException e) {
            respond user.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*' { respond user, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond userService.get(id)
    }

    def update(User user) {
        if (user == null) {
            notFound()
            return
        }
        def file = request.getFile("filename")
        String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), file);
        name+=".jpg"
        try{
            if(file && !file.empty){
                file.transferTo(new File(grailsApplication.config.maconfig.assets_path+"${name}"))
                flash.message="your.sucessful.file.upload.message"
            }
            else{
                flash.message="your.unsucessful.file.upload.message"
            }
        }
        catch(Exception e){
            log.error("Your exception message goes here",e)
        }

        user.thumbnail=new Illustration(filename: name)
        try {
            userService.save(user)
        } catch (ValidationException e) {
            respond user.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*'{ respond user, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        userService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
