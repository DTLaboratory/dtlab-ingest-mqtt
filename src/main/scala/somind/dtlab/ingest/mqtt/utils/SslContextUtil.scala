package somind.dtlab.ingest.mqtt.utils

import java.io.FileInputStream
import java.security.KeyStore

import com.typesafe.scalalogging.LazyLogging
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import somind.dtlab.ingest.mqtt.Conf.{keyStorePassword, keyStorePath}

object SslContextUtil extends LazyLogging {

  def apply(): SSLContext = {

    val keyStore = KeyStore.getInstance("pkcs12")
    keyStore.load(new FileInputStream(keyStorePath),
      keyStorePassword.toCharArray)
    val kmf =
      KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    kmf.init(keyStore, keyStorePassword.toCharArray)

    val trustManagerFactory =
      TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    trustManagerFactory.init(keyStore)

    val ctx: SSLContext = SSLContext.getInstance("TLS")
    ctx.init(kmf.getKeyManagers, trustManagerFactory.getTrustManagers, null)
    ctx

  }

}
