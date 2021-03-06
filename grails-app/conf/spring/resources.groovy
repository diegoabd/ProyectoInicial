// Place your Spring DSL code here

import com.mercadolibre.opensource.frameworks.restclient.SimpleRestClient
import itemsconsumer2.ItemsConsumerService
import mercadolibre.ItemsRepository
import mercadolibre.RESTItemClient
import mercadolibre.RedisMananger

beans = {

    restClient(SimpleRestClient){bean ->
        name= "prod Rest client"
        baseUrl= "https://api.mercadolibre.com"
        bean.initMethod= "init"
        bean.scope="singleton"
        soTimeout= 50000
    }

    itemsClient(RESTItemClient){
        restClient = ref('restClient')
    }

    itemsRepository(ItemsRepository){
    }

    redisMananger(RedisMananger){
    }

    itemsConsumerService(ItemsConsumerService){
        itemsClient = ref('itemsClient')
        itemsRepository= ref('itemsRepository')
        redisMananger = ref('redisMananger')
    }

    itemsConsumerServiceJob(ItemsConsumerServiceJob){
        itemsConsumerService = ref('itemsConsumerService')
    }


}