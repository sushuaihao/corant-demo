# Corant JCache Caffeine Demo

# Tutorial
## Add JCache Caffeine dependency
```
<dependency>
    <groupId>org.corant</groupId>
    <artifactId>corant-suites-jcache-caffeine</artifactId>
    <version>1.2.5-SNAPSHOT</version>
</dependency>
```
more details see the `pom.xml`
## JCache annotations usage
```java
  @CachePut(cacheName = "myCache")
  public String cachePut(@CacheValue String value, @CacheKey String key) {
    return value;
  }

  @CacheResult(cacheName = "myCache")
  public String cacheResult(@CacheKey String key) {
    /*
      if parameter key got no cache value , then invoke this method.
      And the value cached by this parameter key.
    */
    logger.info("not found value in cache , now invoke method and cached returned value");
    return null;
  }
```
Necessary, you must add annotation's Interceptors in `beans.xml`.
```xml
<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_2_0.xsd"
       bean-discovery-mode="annotated">
    <interceptors>
        <class>org.jsr107.ri.annotations.cdi.CacheResultInterceptor</class>
        <class>org.jsr107.ri.annotations.cdi.CachePutInterceptor</class>
        <class>org.jsr107.ri.annotations.cdi.CacheRemoveEntryInterceptor</class>
        <class>org.jsr107.ri.annotations.cdi.CacheRemoveAllInterceptor</class>
    </interceptors>
</beans>
```


## Caffeine configurations in corant
You can config cache directly or its configuration's file path in `META-INF/application.properties` 
* cache configuration's file path in `META-INF/application.properties`:
```
caffeine.config.resource=META-INF/caffeine.properties
```
Then you can in `META-INF/caffeine.properties` config your cache.Format `.conf/.json/.properties` are supported.
* cache config in `META-INF/application.properties`
```
caffeine.jcache.myCache.policy.maximum.size=123
```
See the [reference.conf](https://github.com/ben-manes/caffeine/blob/master/jcache/src/main/resources/reference.conf) for more details.
## Cache programmatic API
You can `@Inject` CacheManager or CachingProvider.
```java
public class CachingProviderTest {
  @Inject CachingProvider cachingProvider;

  @Inject
  @SURI("hello")
  CacheManager cacheManager;
}
```
Required `@SURI`, its value defined for CacheManager's uri.
Then you can use CacheManager create/get cache in your code.
```java
Cache<Object, Object> cache = cacheManager.getCache("apple");
```
Note the above example `apple` named cache required config in your conf file.

eg.
```
caffeine.jcache.apple.policy.maximum.size=345
```
Caffeine will create `apple` named cache when you used `CacheManager.getCache("apple")`.



