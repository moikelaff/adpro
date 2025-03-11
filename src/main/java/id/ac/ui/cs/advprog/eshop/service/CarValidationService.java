package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Service;


@Service
public class CarValidationService {
    public boolean isValid(Car car) {
        return car.getCarName() != null && !car.getCarName().trim().isEmpty();
    }

    public String getValidationMessage(Car car) {
        if (!isValid(car)) {
            return "Car name cannot be empty";
        }
        return null;
    }
}
