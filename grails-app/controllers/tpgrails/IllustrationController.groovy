package tpgrails

import grails.validation.ValidationException
import org.apache.commons.lang.RandomStringUtils

import static org.springframework.http.HttpStatus.*

class IllustrationController {

    IllustrationService illustrationService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond illustrationService.list(params), model:[illustrationCount: illustrationService.count()]
    }

    def show(Long id) {
        respond illustrationService.get(id)
    }

    def create() {
        respond new Illustration(params)
    }

    def save(Illustration illustration) {
        if (illustration == null) {
            notFound()
            return
        }

        def file = request.getFile("filename")
       // String imageUploadPath=grailsApplication.config.maconfig.assets_path
        // generer un nom de nfichir aléatoire et verifier qu'il n'existe pas deja
        // Sauvegarder le fichier sur le disque en utilisté
        //file.transferTo(new File("${imageUploadPath}/${file.name}"))
        String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), file);
        name+=".jpg"

        /* Create storage path directory if it does not exist
        def storagePath = servletContext.getRealPath(imageUploadPath)
        def storagePathDirectory = new File(storagePath)
        if (!storagePathDirectory.exists()) {
            print "CREATING DIRECTORY ${storagePath}: "
            if (storagePathDirectory.mkdirs()) {
                println "SUCCESS"
            } else {
                println "FAILED"
            }
        }
*/
        try{
            if(file && !file.empty){
             //   file.transferTo(new File("D:/M2/U6_FrameworkJava/GrailsTP/GrailsTP/grails-app/assets/images/${name}"))
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
        //garder une trace sur un le nom fichier
        illustration= new Illustration(filename: name)

       // user.thumbnail= new Illustration(filename: 'image.png')l

        try {
            illustrationService.save(illustration)
        } catch (ValidationException e) {
            respond illustration.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'illustration.label', default: 'Illustration'), illustration.id])
                redirect illustration
            }
            '*' { respond illustration, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond illustrationService.get(id)
    }

    def update(Illustration illustration) {
        if (illustration == null) {
            notFound()
            return
        }

        try {
            illustrationService.save(illustration)
        } catch (ValidationException e) {
            respond illustration.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'illustration.label', default: 'Illustration'), illustration.id])
                redirect illustration
            }
            '*'{ respond illustration, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        illustrationService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'illustration.label', default: 'Illustration'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'illustration.label', default: 'Illustration'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
