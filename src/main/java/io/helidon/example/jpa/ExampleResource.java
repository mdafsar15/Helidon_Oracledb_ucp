package io.helidon.example.jpa;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Properties;

import javax.enterprise.context.Dependent;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
//import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
//import org.hibernate.query.Query;

/**
 * Simple JAXRS resource class.
 */
@Dependent
@Path("/example")
public class ExampleResource {
	@PersistenceContext
	private EntityManager em;

	@Produces("text/plain")
	@Path("hello")
	@GET
	public String get() {
		return "It works!";
	}

	@GET
	@Path("response/{salutation}")
	@Produces("text/plain")
	@Transactional
	public String getResponse(@PathParam("salutation") String salutation) {
		System.out.println("in method getResponse...");
		final Greeting greeting = this.em.find(Greeting.class, salutation);
		final String returnValue;
		if (greeting == null) {
			System.out.println("greeting is null");
			returnValue = null;
		} else {
			returnValue = greeting.getResponse();
			System.out.println("response is: " + returnValue);
		}
		return returnValue;
	}

	@GET
	@Path("get")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Greeting> getAll() {
		System.out.println("in method getResponse...");
		final Query greeting = this.em.createNativeQuery("Select SALUTATION,RESPONSE from GREETING");
		final List<Greeting> returnValue;
		if (greeting == null) {
			System.out.println("greeting is null");
			returnValue = null;
		} else {
			returnValue = greeting.getResultList();
			System.out.println("response is: " + returnValue);
		}
		return returnValue;
	}

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@POST
	@Path("post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	@PersistenceContext
	public Greeting insert(@RequestBody Greeting greeting) throws NamingException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		System.out.println("in method getResponse..." + greeting);

//		Properties props = new Properties();
//		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "java:comp/UserTransaction");
//		Context initialContext = new InitialContext(props);
		
		
		UserTransaction transaction = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
		transaction.begin();
//		EntityManager em = getEntityManager();
		em.persist(greeting);
		transaction.commit();

//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//		EntityTransaction entityTransaction = entityManager.getTransaction();
//
//		entityTransaction.begin();
//		entityManager.persist(greeting);
//		entityTransaction.commit();
//
//		entityManager.close();

		System.out.println("data successfully insert!!!" + greeting);
		return greeting;

	}
//	Query query = em.createNativeQuery("INSERT INTO Bike (id, name) VALUES (:id , :name);");
//	em.getTransaction().begin();
//	query.setParameter("id", "5");
//	query.setParameter("name", "Harley");
//	query.executeUpdate();
//	em.getTransaction().commit();

//	@POST
//	@Path("insert")
//	@Produces("text/plain")
//	@Transactional
//	public void getInsert() {
//		System.out.println("in method Insert()");
//		String insertQuery = "INSERT INTO GREETING (SALUTATION, RESPONSE) VALUES ('Hp', 'Dell')";
//		final Query greeting = this.em.createQuery(insertQuery);
//		final String returnValue;
//		if (greeting == null) {
//			System.out.println("greeting is null");
//			returnValue = null;
//		} else {
////			returnValue = greeting.getResponse();
//			System.out.println("response is: " + insertQuery);
//		}
////		return returnValue;
//	}

//	@GET
//	@Path("/getAll")
//	@Produces("text/plain")
//	@Transactional
//	public List<Greeting> getUser() {
//		Session sess = em.unwrap(Session.class);
//		Query emailFetchQuery = sess.createQuery("Select SALUTATION from GREETING");
////		emailFetchQuery.setParameter("email", email);
//		return emailFetchQuery.getResultList();
//	}
}
