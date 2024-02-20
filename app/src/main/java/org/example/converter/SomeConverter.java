package org.example.converter;

import org.example.model.SomeViewModel;
import org.example.request.SomeRequest;

public class SomeConverter {

    public SomeRequest toEmploymentRequest(SomeViewModel viewModel) {
        return new SomeRequest(
                viewModel.getName(),
                viewModel.getPosition(),
                viewModel.getAnnualSalary()
        );
    }
}
