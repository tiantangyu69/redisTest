
    /**
     * 创建 RestTemplate
     *
     * @param factory
     * @return
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

        List<HttpMessageConverter<?>> httpMessageConverters = Lists.newArrayList();

        httpMessageConverters.add(new FormHttpMessageConverter());

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, MediaType.APPLICATION_OCTET_STREAM));
        httpMessageConverters.add(stringHttpMessageConverter);

        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8, MediaType.ALL));
        httpMessageConverters.add(fastJsonHttpMessageConverter);

        restTemplate.setMessageConverters(httpMessageConverters);

        return restTemplate;
    }

    /**
     * 创建 HttpComponentsClientHttpRequestFactory
     *
     * @param httpClient
     * @return
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(5000);//单位为ms
        factory.setReadTimeout(10000);//单位为ms
        return factory;
    }

    /**
     * 创建 HttpClient
     *
     * @param httpClientConnectionManager
     * @return
     */
    @Bean
    public HttpClient httpClient(HttpClientConnectionManager httpClientConnectionManager) {
        List<Header> defaultHeaders = Lists.newArrayList();
        defaultHeaders.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        defaultHeaders.add(new BasicHeader("Accept-Language", "zh-CN"));

        return HttpClientBuilder.create()
                .setConnectionManager(httpClientConnectionManager)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .setDefaultHeaders(defaultHeaders)
                .build();
    }

    /**
     * 创建 HttpClientConnectionManager
     *
     * @return
     */
    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(1000);
        clientConnectionManager.setDefaultMaxPerRoute(200);

        return clientConnectionManager;
    }
