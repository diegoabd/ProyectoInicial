package itemsconsumer2

class Logs {

    String iditem
    String description
    String fecha

    static constraints = {
    }

    static mapping = {
        version false
        table 'Logs'
        id generator: "increment"
    }

    @Override
    public String toString() {
        return "Logs[iditem:" + iditem + "][description:" +
                description + "][fecha:" + fecha + "]";
    }

}
