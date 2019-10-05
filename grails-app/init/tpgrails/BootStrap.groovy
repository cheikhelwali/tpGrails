package tpgrails

class BootStrap {

    def init = { servletContext ->
       def userInstance = new User(username: "Cheikh",
                password: "password",
                thumbnail: new Illustration(filename: "advancedgrails.svg"))
        .save(flush: true, failOnError: true)
        (1..5).each {
            def annonceInstance=  new Annonce(
                            title: "title",
                            description: "description",
                            validTill: new Date(),
                            state: Boolean.TRUE)
                    .addToIllustrations(new Illustration(filename: "advancedgrails.svg"))
                    .addToIllustrations(new Illustration(filename: "advancedgrails.svg"))
                    .addToIllustrations(new Illustration(filename: "advancedgrails.svg"))
            userInstance.addToAnnonces(annonceInstance)
        }
        userInstance.save(flush:true, failOnError:true)
    }
    def destroy = {
    }
}
