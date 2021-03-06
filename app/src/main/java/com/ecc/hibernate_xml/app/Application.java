package com.ecc.hibernate_xml.app;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.ecc.hibernate_xml.util.function.CheckedUnaryOperator;
import com.ecc.hibernate_xml.util.dao.HibernateUtility;
import com.ecc.hibernate_xml.util.app.UiRouter;
import com.ecc.hibernate_xml.util.app.Menu;

public class Application {
	private static String MAIN_HEADER = "--------------------------\nExercise 7 - Hibernate XML\nPerson Registration System\nBy Emmett Young\n---------------------------";
	private static PersonUiHandler personUiHandler = new PersonUiHandler();
	private static PersonContactUiHandler personContactUiHandler = new PersonContactUiHandler();
	private static PersonRoleUiHandler personRoleUiHandler = new PersonRoleUiHandler();
	private static RoleUiHandler roleUiHandler = new RoleUiHandler();
	private static UiRouter uiRouter;

	static {
		Menu menu = new Menu()
			.add(new Menu("Person Registry.")
				.add(new Menu("List Person records.")
					.add(new Menu("Sort by GWA."))
					.add(new Menu("Sort by date hired."))
					.add(new Menu("Sort by last name.")))
				.add(new Menu("Create a new Person.")
					.add(new Menu("Change name."))
					.add(new Menu("Change address."))
					.add(new Menu("Change birthday."))
					.add(new Menu("Change GWA."))
					.add(new Menu("Change employment status."))
					.add(new Menu("Manage contact information.")
						.add(new Menu("List contact information."))
						.add(new Menu("Add contact information.")
							.add(new Menu("Add landline."))
							.add(new Menu("Add email."))
							.add(new Menu("Add mobile number.")))
						.add(new Menu("Update contact information."))
						.add(new Menu("Delete contact information.")))
					.add(new Menu("Manage roles.")
						.add(new Menu("List roles."))
						.add(new Menu("Add role."))
						.add(new Menu("Remove role."))))
				.add(new Menu("Update an existing Person record.")
					.add(new Menu("Change name."))
					.add(new Menu("Change address."))
					.add(new Menu("Change birthday."))
					.add(new Menu("Change GWA."))
					.add(new Menu("Change employment status."))
					.add(new Menu("Manage contact information.")
						.add(new Menu("List contact information."))
						.add(new Menu("Add contact information.")
							.add(new Menu("Add landline."))
							.add(new Menu("Add email."))
							.add(new Menu("Add mobile number.")))
						.add(new Menu("Update contact information."))
						.add(new Menu("Delete contact information.")))
					.add(new Menu("Manage roles.")
						.add(new Menu("List roles."))
						.add(new Menu("Add role."))
						.add(new Menu("Remove role."))))
				.add(new Menu("Delete an existing Person record.")))
			.add(new Menu("Role Registry.")
				.add(new Menu("List Role records."))
				.add(new Menu("Create a new Role record."))
				.add(new Menu("Update an existing Role record."))
				.add(new Menu("Delete an existing Role record.")));
		uiRouter = new UiRouter(menu);
	}

	public static void main(String[] args) {
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		System.out.println(MAIN_HEADER);

		HibernateUtility.initializeSessionFactory();
		registerRoutes();
		uiRouter.run();
		HibernateUtility.closeSessionFactory();
	}

	public static void registerRoutes() {
		// No passing of parameters.
		uiRouter.register("Sort by GWA.", personUiHandler::listByGWA);
		uiRouter.register("Sort by date hired.", personUiHandler::listByDateHired);
		uiRouter.register("Sort by last name.", personUiHandler::listByLastName);
		uiRouter.register("Delete an existing Person record.", personUiHandler::delete);

		uiRouter.register("List Role records.", roleUiHandler::list);
		uiRouter.register("Create a new Role record.", roleUiHandler::create);
		uiRouter.register("Update an existing Role record.", roleUiHandler::update);
		uiRouter.register("Delete an existing Role record.", roleUiHandler::delete);

		// Passes the Person detached instance to the next level.
		uiRouter.register("Create a new Person.", personUiHandler::create, personUiHandler::displayPerson);
		uiRouter.register("Update an existing Person record.", personUiHandler::update, personUiHandler::displayPerson);

		// Receives the Person detached instance from the previous level.
		uiRouter.register("Change name.", personUiHandler::changeName);
		uiRouter.register("Change address.", personUiHandler::changeAddress);
		uiRouter.register("Change birthday.", personUiHandler::changeBirthday);
		uiRouter.register("Change GWA.", personUiHandler::changeGWA);
		uiRouter.register("Change employment status.", personUiHandler::changeEmploymentStatus);

		// Receives the Person detached instance from the previous level and passes it to the next level.
		uiRouter.register("Manage contact information.", CheckedUnaryOperator.identity());
		uiRouter.register("Manage roles.", CheckedUnaryOperator.identity());

		// Receives the Person detached instance from the previous level.
		uiRouter.register("List contact information.", personContactUiHandler::list);
		uiRouter.register("Update contact information.", personContactUiHandler::update);
		uiRouter.register("Delete contact information.", personContactUiHandler::delete);
		uiRouter.register("List roles.", personRoleUiHandler::list);
		uiRouter.register("Add role.", personRoleUiHandler::add);
		uiRouter.register("Remove role.", personRoleUiHandler::remove);

		// Receives the Person detached instance from the previous level and passes it to the next level.
		uiRouter.register("Add contact information.", CheckedUnaryOperator.identity());

		// Receives the Person detached instance from the previous level.
		uiRouter.register("Add landline.", arg -> { personContactUiHandler.create(arg, "Landline"); });
		uiRouter.register("Add email.", arg -> { personContactUiHandler.create(arg, "Email"); });
		uiRouter.register("Add mobile number.", arg -> { personContactUiHandler.create(arg, "Mobile"); });
	}
}