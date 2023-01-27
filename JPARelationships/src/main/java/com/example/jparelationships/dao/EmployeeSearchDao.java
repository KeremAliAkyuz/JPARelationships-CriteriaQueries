package com.example.jparelationships.dao;

import com.example.jparelationships.employee.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeSearchDao {

    private final EntityManager entityManager;//autowired yerine RequiredArgsConsturcutor kullanılarak final bir şekilde inject edilebilir

    public List<Employee> findAllBySimpleQuery(
            String firstName,
            String lastName,
            String eMail
    ){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        //select * from employee
        Root<Employee> root = criteriaQuery.from(Employee.class);

        //prepare WHERE clause
        //WHERE firstname like '%ali%'
        Predicate firstNamePredicate = criteriaBuilder
                .like(root.get("firstName"),"%" + firstName + "%");//refers column name in the database
        Predicate lastNamePredicate = criteriaBuilder
                .like(root.get("lastName"),"%" + lastName + "%");
        Predicate eMail1Predicate = criteriaBuilder
                .like(root.get("eMail"),"%" + eMail + "%");
        Predicate firstNameOrLastNamePredicate = criteriaBuilder
                .or(firstNamePredicate, lastNamePredicate);
        // => final query ==> select * from employee where firstname like '%ali%' or lastname  like '%ali%'
        //  and email like '%email%'
        var andEmailPredicate = criteriaBuilder.and(firstNameOrLastNamePredicate,eMail1Predicate);
        criteriaQuery.where(andEmailPredicate);
        TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);//şimdiye kadarki yaptığımız tüm criteria querryleri kullanmak için

        return query.getResultList();
    }

    public List<Employee> findAllByCriteria(
            SearchRequest request //In this request we don't know if we have values or not
    ){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        //select from employee
        Root<Employee> root = criteriaQuery.from(Employee.class);
        if(request.getFirstName() !=null){
            Predicate firstNamePredicate = criteriaBuilder
                    .like(root.get("firstName"),"%"+request.getFirstName()+"%");
            predicates.add(firstNamePredicate);
        }
        if(request.getFirstName() !=null){
            Predicate lastNamePredicate = criteriaBuilder
                    .like(root.get("lastName"),"%"+request.getLastName()+"%");
            predicates.add(lastNamePredicate);
        }
        if(request.getEMail() !=null){
            Predicate eMailPredicate = criteriaBuilder
                    .like(root.get("eMail"),"%"+request.getEMail()+"%");
            predicates.add(eMailPredicate);
        }
        criteriaQuery.where(
                criteriaBuilder.or(predicates.toArray(new Predicate[0]))//this method will return of table of predicates
        );
        TypedQuery<Employee> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
