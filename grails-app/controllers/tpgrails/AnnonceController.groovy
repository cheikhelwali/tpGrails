package tpgrails

import grails.validation.ValidationException
import org.apache.commons.lang.RandomStringUtils

import static org.springframework.http.HttpStatus.*

class AnnonceController {

    AnnonceService annonceService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond annonceService.list(params), model:[annonceCount: annonceService.count()]
    }

    def show(Long id) {
        respond annonceService.get(id)
    }

    def create() {
        respond new Annonce(params)
    }

    def save(Annonce annonce) {
        //List IllustrationList= new ArrayList<Illustration>()
        if (annonce == null) {
            notFound()
            return
        }
        def file = request.getFiles("filename")
        file.each{
            String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), it);
            name+=".jpg"
            try{
                if(it && !it.empty){
                    it.transferTo(new File(grailsApplication.config.maconfig.assets_path+"${name}"))
                    flash.message="your.sucessful.file.upload.message"
                }
                else{
                    flash.message="your.unsucessful.file.upload.message"
                }
            }
            catch(Exception e){
                log.error("Your exception message goes here",e)
            }
            annonce.addToIllustrations(new Illustration(filename: name))
        }


        try {
            annonceService.save(annonce)
        } catch (ValidationException e) {
            respond annonce.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'annonce.label', default: 'Annonce'), annonce.id])
                redirect annonce
            }
            '*' { respond annonce, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond annonceService.get(id)
    }

    def update(Annonce annonce) {
        if (annonce == null) {
            notFound()
            return
        }

        try {
            annonceService.save(annonce)
        } catch (ValidationException e) {
            respond annonce.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'annonce.label', default: 'Annonce'), annonce.id])
                redirect annonce
            }
            '*'{ respond annonce, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        annonceService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'annonce.label', default: 'Annonce'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'annonce.label', default: 'Annonce'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def deleteIllustration(){
        def illustrationId = params.illistrationId
        def annoceId = params.annonceId
        def annonceInstance = Annonce.get(annoceId)
        def illustrationInstance = Illustration.get(illustrationId)
        annonceInstance.removeFromIllustrations(illustrationInstance)
        annonceInstance.save(flush: true)
        // il faut effacer le fichier physique sur le disque
        illustrationInstance.delete(flush: true)
        redirect annonceInstance
    }

}
