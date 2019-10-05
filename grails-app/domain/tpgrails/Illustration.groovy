package tpgrails

class Illustration {

    String filename

    static constraints = {
        filename blank: false
    }

    @Override
    String toString(){
        return filename
    }
}
