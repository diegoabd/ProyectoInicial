package itemsconsumer2



class ItemsConsumerServiceJob {
    def itemsConsumerService
    static triggers = {
      simple repeatInterval: 50000 // execute job once in 5 seconds
    }

    def execute() {
        // execute job

        itemsConsumerService.executeProcesMessaje()
    }
}
