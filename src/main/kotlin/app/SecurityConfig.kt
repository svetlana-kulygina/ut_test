package app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.*
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("ADMIN")
            .and().withUser("user").password("user").roles("USER")
            .and().withUser("other").password("other").roles("OTHER")
            .and().passwordEncoder(NoOpPasswordEncoder.getInstance())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/getLogs**").authenticated()
                .antMatchers(HttpMethod.POST,"/add**").hasAnyRole("USER", "ADMIN")
            .and().formLogin().failureForwardUrl("/login?error").defaultSuccessUrl("/", false)
            .and().logout().invalidateHttpSession(true).logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll()
            .and().exceptionHandling().accessDeniedPage("/accessError")
            .and().csrf().disable()
    }
}