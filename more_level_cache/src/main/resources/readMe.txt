业务需求：因数据量较大本机内存不够用,将经常需要使用的数据放入本地缓存(ehCache),将一些使用频率稍低的数据放入redis,以减少本机内存的使用量.

下面给出具体的操作步骤
1缓存的配置类 (MoreCacheConfig)
 1.1 ehCache 缓存的配置
     /**
      *
      * 配置ehCache cacheManager的实现
      */
     @Bean
     @Primary
     public CacheManager ehCacheManager() {
         return new EhCacheCacheManager(ehCacheManagerFactory().getObject());
     }

     @Bean
     public EhCacheManagerFactoryBean ehCacheManagerFactory() {
         EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
         factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
         factoryBean.setShared(true);
         return factoryBean;
     }

  切记不能采用如下的配置，在此配置下ehCache的持久化不会生成.index的文件，这会导致在系统重启不会从本地文件加载数据
      @Bean
      public CacheManager ehCacheManager() {
          net.sf.ehcache.CacheManager buildCacheManager =   EhCacheManagerUtils.buildCacheManager();
          EhCacheCacheManager ehc=new EhCacheCacheManager(buildCacheManager);
          return ehc;
      }

 ehCache文件生成规则：
   当程序启动时会生成一个 ehcache-diskstore.lock 和对应的.data文件，定义了几个缓存就有几个.data文件
   当程序正常关闭时ehcache-diskstore.lock被删除，与.data文件对应的.index文件将会被创建.


 1.2 redis 缓存的配置
     /**
         * redis的 cacheManager 实现
         *
         * @param redisConnectionFactory
         */
        @Bean
        public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
            RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
            // .entryTtl(Duration.ofMinutes(10)); // 设置 Redis 缓存过期时间为10分钟
            return RedisCacheManager.builder(redisConnectionFactory)
                    .cacheDefaults(redisCacheConfiguration)
                    .build();
        }

        /**
         * <p>
           description:配置redis的连接工厂<br>
         　 author chenzhizhe<br>
         　 date 2024/4/28 10:41 <br>
         *<p/>
         　* @param
         　 @return org.springframework.data.redis.connection.RedisConnectionFactory
        　*/
        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
                RedisStandaloneConfiguration redisStandaloneConfiguration =
                        new RedisStandaloneConfiguration();
                redisStandaloneConfiguration.setHostName(host);
                redisStandaloneConfiguration.setDatabase(0);
                if(password!=null&&!"".equals(password)) {
                    redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
                }
                redisStandaloneConfiguration.setPort(port);
               // JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);

              JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder().usePooling();
              jedisClientConfiguration.poolConfig(jedisPoolConfig());
              JedisConnectionFactory jedisConnectionFactory= new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
              jedisConnectionFactory.afterPropertiesSet();
              return  jedisConnectionFactory;
        }

        @Bean
        public JedisPoolConfig jedisPoolConfig() {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(Integer.parseInt(env.getProperty("spring.redis.jedis.pool.max-active")));
            poolConfig.setMinIdle(Integer.parseInt(env.getProperty("spring.redis.jedis.pool.min-idle")));
            poolConfig.setMaxWaitMillis(Long.parseLong(env.getProperty("spring.redis.jedis.pool.max-wait")));
            return poolConfig;
        }


2. 动态实现bean作用域
      默认的缓存实现类KlineDataSourceEhCacheImpl，当实现多个缓存整合时，需要将该类动态加载为多例模式，每个实例在注入不同的
    caCheManager来实现不同的缓存实现。
    详情请查看 CustomScopeConfigurer的实现


3.业务类的实现(KlineMoreLevelCacheImpl)
     在该类初始化将不同caCheManager注入到不同的KlineDataSourceEhCacheImpl的实例，然后根据业务动态选择用哪一个KlineDataSourceEhCacheImpl
    来实现不同数据存不同的缓存的目的.

4.数据测试
    4.1 写数据到本地缓存
    http://localhost:8083/ehcache/int_kline_date?count=20000&prodCode=AAPL.SS&period=3
    4.2 写数据到redis缓存
    http://localhost:8083/ehcache/int_kline_date?count=20000&prodCode=AAPL.O&period=3
    4.3 读数据从本地缓存
    http://localhost:8083/ehcache/list_kline_date?prodCode=AAPL.SS&period=3
    4.4 读数据从redis缓存
    http://localhost:8083/ehcache/list_kline_date?prodCode=AAPL.O&period=3
