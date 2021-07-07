package ru.unit.techno.device.registration.core.impl.resource;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unit.techno.device.registration.core.impl.dto.RegistrationDto;
import ru.unit.techno.device.registration.core.impl.service.RegistrationService;

@RequiredArgsConstructor
@RequestMapping("/${spring.application.name}/api")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public Long createUser(@RequestBody RegistrationDto registrationDto) {
        return registrationService.registerGroup(registrationDto);
    }
}
