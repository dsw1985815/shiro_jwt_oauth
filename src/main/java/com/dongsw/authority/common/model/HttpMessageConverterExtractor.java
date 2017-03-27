package com.dongsw.authority.common.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Response extractor that uses the given {@linkplain HttpMessageConverter entity converters}
 * to convert the response into a type {@code T}.
 *
 * @author Arjen Poutsma
 * @see RestTemplate
 * @since 3.0
 */
public class HttpMessageConverterExtractor<T> implements ResponseExtractor<T> {

    private final Type responseType;

    private final Class<T> responseClass;

    private final List<HttpMessageConverter<?>> messageConverters;

    private final Log logger;


    /**
     * Create a new instance of the {@code HttpMessageConverterExtractor} with the given response
     * type and message converters. The given converters must support the response type.
     */
    public HttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters) {
        this((Type) responseType, messageConverters);
    }

    /**
     * Creates a new instance of the {@code HttpMessageConverterExtractor} with the given response
     * type and message converters. The given converters must support the response type.
     */
    public HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters) {
        this(responseType, messageConverters, LogFactory.getLog(HttpMessageConverterExtractor.class));
    }

    @SuppressWarnings("unchecked")
    HttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters, Log logger) {
        Assert.notNull(responseType, "'responseType' must not be null");
        Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
        this.responseType = responseType;
        this.responseClass = (responseType instanceof Class) ? (Class<T>) responseType : null;
        this.messageConverters = messageConverters;
        this.logger = logger;
    }


    @Override
    @SuppressWarnings({"unchecked", "rawtypes", "resource"})
    public T extractData(ClientHttpResponse response) throws IOException {
        MessageBodyClientHttpResponseWrapper responseWrapper = new MessageBodyClientHttpResponseWrapper(response);
        if (!responseWrapper.hasMessageBody() || responseWrapper.hasEmptyMessageBody()) {
            return null;
        }
        MediaType contentType = getContentType(responseWrapper);

        for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
            GenericHttpMessageConverter<?> genericMessageConverter = null;
            if (messageConverter instanceof GenericHttpMessageConverter) {
                genericMessageConverter = (GenericHttpMessageConverter<?>) messageConverter;
            }

            T t = read(genericMessageConverter,contentType,messageConverter,responseWrapper) ;
            if(t!=null){
                return t;
            }
        }
        throw new RestClientException("Could not extract response: no suitable HttpMessageConverter found " +
                "for response type [" + this.responseType + "] and content type [" + contentType + "]");
    }


    private T read(GenericHttpMessageConverter<?> genericMessageConverter,MediaType contentType,HttpMessageConverter<?> messageConverter,MessageBodyClientHttpResponseWrapper responseWrapper) throws IOException {
        if (genericMessageConverter != null && genericMessageConverter.canRead(this.responseType, null, contentType)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Reading [" + this.responseType + "] as \"" +
                        contentType + "\" using [" + messageConverter + "]");
            }
            return (T)genericMessageConverter.read(this.responseType, null, responseWrapper);
        }

        if (this.responseClass != null && messageConverter.canRead(this.responseClass, contentType)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Reading [" + this.responseClass.getName() + "] as \"" +
                        contentType + "\" using [" + messageConverter + "]");
            }
            return (T)messageConverter.read((Class) this.responseClass, responseWrapper);
        }

        return null;
    }

    private MediaType getContentType(ClientHttpResponse response) {
        MediaType contentType = response.getHeaders().getContentType();
        if (contentType == null) {
            if (logger.isTraceEnabled()) {
                logger.trace("No Content-Type header found, defaulting to application/octet-stream");
            }
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return contentType;
    }

}