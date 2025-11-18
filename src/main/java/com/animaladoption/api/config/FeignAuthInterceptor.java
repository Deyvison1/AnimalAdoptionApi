package com.animaladoption.api.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Interceptor Feign responsável por propagar o token JWT do usuário autenticado
 * nas requisições entre microsserviços.
 *
 * <p>Se não houver contexto de segurança (ex: chamada interna, agendada ou teste),
 * o interceptor ignora silenciosamente, evitando erros e mantendo logs descritivos.</p>
 */
@Slf4j
@Configuration
public class FeignAuthInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                try {
                    // Captura o contexto de segurança atual
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                    // Verifica se é um token JWT válido
                    if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                        String tokenValue = jwtAuth.getToken().getTokenValue();

                        if (tokenValue != null && !tokenValue.isBlank()) {
                            template.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);

                            if (log.isDebugEnabled()) {
                                log.debug("[FeignAuthInterceptor] Token JWT propagado para chamada Feign -> {}",
                                        template.url());
                            }
                        } else {
                            log.warn("[FeignAuthInterceptor] Token JWT vazio encontrado. "
                                    + "Requisição Feign seguirá sem autenticação: {}", template.url());
                        }

                    } else {
                        if (log.isTraceEnabled()) {
                            log.trace("[FeignAuthInterceptor] Nenhum JwtAuthenticationToken no contexto. "
                                    + "Requisição Feign para {} seguirá sem autenticação.", template.url());
                        }
                    }

                } catch (IllegalStateException | UnsupportedOperationException ex) {
                    // Ocorrências típicas quando o contexto não está disponível (ex: threads async)
                    if (log.isDebugEnabled()) {
                        log.debug("[FeignAuthInterceptor] Contexto de segurança indisponível (thread paralela ou sem requisição ativa).");
                    }
                } catch (Exception ex) {
                    log.error("[FeignAuthInterceptor] Erro inesperado ao propagar token JWT para Feign: {}",
                            ex.getMessage(), ex);
                }
            }
        };
    }
}
