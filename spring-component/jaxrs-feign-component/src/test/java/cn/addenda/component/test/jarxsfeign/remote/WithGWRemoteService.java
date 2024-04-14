package cn.addenda.component.test.jarxsfeign.remote;

import cn.addenda.component.jaxrsfeign.SimpleFeignClient;

import javax.ws.rs.*;

@SimpleFeignClient(gateWay = "${baidu.gateway.address}",
        connectTimeoutConfig = "${connectTimeoutConfig}", readTimeoutConfig = "${readTimeoutConfig}",
        clientConfig = "apacheHttpClient", decoderConfig = "simpleStringDecoder",
        dismissException = true, mapNullToDefault = true)
public interface WithGWRemoteService {

  @GET
  @Path("/")
  String baidu();

}