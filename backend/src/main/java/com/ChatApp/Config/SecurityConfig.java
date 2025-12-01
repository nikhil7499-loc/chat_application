package com.ChatApp.config;









@Configuration
@EnablewebSecurity

public class SecurityConfig {
    @Value("${app.cors.allowed-original}")
    private String[] allowedOrigins;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationfilter jwtAuthenticationfilter){
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;
    }
    
    @Bean
    public SedcuritryFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
        .csrf(csrf->csrf.disable())
        .cors(Customizer.withDefaults())
        .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth->auth.requestMatchers("/**").permitAll());
        
        return http.build();
    }

    @Bean public WebMyConfigurer corConfigurer(){
        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(CorsRegistry registry){
                regidtry.addMapping("/**").allowedOrigins(allowedorigins).allowedMethod("GET","POST","PUT","DELETE","OPTIONS","PATCH").allowedHeaders("*").alloweCredentials(true);
            }
        }
    }
}
