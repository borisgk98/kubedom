package space.borisgk98.kubedom.api.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@UtilityClass
public class HttpUtils {

    public Optional<String> getHeader(HttpRequest request, String header) {
        return getHeader(request.getHeaders(), header);
    }

    public Optional<String> getHeader(HttpHeaders httpHeaders, String header) {
        return Optional.ofNullable(httpHeaders.get(header))
                .map(Collection::stream)
                .map(Stream::findFirst)
                .map(Optional::get);
    }
}
