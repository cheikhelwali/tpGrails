package tpgrails

import grails.converters.JSON
import grails.converters.XML

import java.text.SimpleDateFormat

class ApiController {

    AnnonceService annonceService
    def pattern = "dd-MM-yyyy"

    def Annonce() {
        switch (request.getMethod()) {
            case "GET":
                if (!params.id)
                    return response.status = 400
                def annonce = Annonce.get(params.id)
                if (!annonce)
                    return response.status = 404
                response.withFormat {
                    json { render annonce as JSON }
                    xml { render annonce as XML }
                }
                break
            case "PUT":
                if (!params.id)
                    return response.status = 400
                def annonce = Annonce.get(params.id)
                if (!annonce)
                    return response.status = 404
                if (!request.JSON.title || !request.JSON.description || !request.JSON.validTill || !request.JSON.state)
                    return response.status = 404
                annonce.title = request.JSON.title
                annonce.description = request.JSON.description
                annonce.validTill = new SimpleDateFormat(pattern).parse(request.JSON.validTill)
                annonce.state = request.JSON.state
                annonceService.save(annonce)
                return response.status = 200
                break

            case "PATCH":
                if (!params.id)
                    return response.status = 400
                def annonce = Annonce.get(params.id)
                if (!annonce)
                    return response.status = 404
                if (request.JSON.validTill)
                    annonce.validTill = (new SimpleDateFormat(pattern).parse(request.JSON.validTill))
                if (request.JSON.state)
                    annonce.state = new Boolean(request.JSON.state)
                if (request.JSON.description)
                    annonce.description = request.JSON.description
                if (request.JSON.title)
                    annonce.title = request.JSON.title
                annonceService.save(annonce)
                return response.status = 200
                break
            case "DELETE":
                if (!params.id)
                    return response.status = 400
                def annonce = Annonce.get(params.id)
                if (!annonce)
                    return response.status = 404
                annonce.delete(flush: true)
                return response.status = 200
                break
            default:
                return response.status = 405
                break
        }
        return response.status = 406
    }

    def Annonces() {
        switch (request.getMethod()) {
            case "GET":
                def annonces = Annonce.getAll()
                if (!annonces)
                    return response.status = 404
                response.withFormat {
                    json { render annonces as JSON }
                    xml { render annonces as XML }
                }
                break
            case "POST":
                if (!request.JSON.title || !request.JSON.description || !request.JSON.validTill || !request.JSON.state)
                    return response.status = 404
                def annonce = new Annonce(
                        title: request.JSON.title,
                        description: request.JSON.description,
                        validTill: new SimpleDateFormat(pattern).parse(request.JSON.validTill),
                        state: new Boolean(request.JSON.state)
                )
                if (request.JSON.userId) {
                    annonce.author = User.get(request.JSON.userId)
                }
                annonceService.save(annonce)
                return response.status = 201
                break
            default:
                return response.status = 405
                break
        }
        return response.status = 406
    }

    def show = {
        if(params.id && Annonce.exists(params.id)){
            render Annonce.findById(params.id) as JSON
        }else{
            render Annonce.list() as JSON
        }
    }

    def save = {
        def annonce = new Annonce(params["annonce"])

        if(annonce.save()){
            render annonce as JSON
        }else{

        }
    }
}



