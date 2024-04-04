package sit.int204.classicmodelsservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int204.classicmodelsservice.dtos.NewCustomerDto;
import sit.int204.classicmodelsservice.entities.Customer;
import sit.int204.classicmodelsservice.repositories.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ListMapper listMapper;
    public Customer findByID(Integer id) {
        return repository.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer number '"+ id + "' does not exist !!!!"));
    }

    public Page<Customer> getCustomers(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<Customer> getCustomers() {
        return repository.findAll();
    }

    public NewCustomerDto createCustomer(NewCustomerDto newCustomer) {
        if(repository.existsById(newCustomer.getCustomerNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate customer for id "+
                    newCustomer.getCustomerNumber());
        }
        Customer customer = mapper.map(newCustomer, Customer.class);
        return mapper.map(repository.saveAndFlush(customer), NewCustomerDto.class);
    }
    public List<NewCustomerDto> getAllCustomers() {
        return listMapper.mapList(repository.findAll(), NewCustomerDto.class, mapper);
    }
}
