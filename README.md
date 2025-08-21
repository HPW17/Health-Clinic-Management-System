# Health Clinic Management System


### About/Overview

This program simulates the operations of a health clinic by managing rooms, patients, clinical and non-clinical staff. It offers functionalities for registering patients, assigning them to rooms, assigning clinical staff to patients, sending patients home, and deactivating clinical staff. The clinic's seating chart and room assignments are dynamically updated based on the operations performed. The model is flexible and extensible, allowing for future modifications or expansions. It can read the initial setup from a clinic specification file and then processes various actions, as demonstrated in the driver class.


### List of Features

The following features are available in both GUI-based and text-based controllers: 

1. Clinic Initialization: Load clinic setup and details from a specification text file.
2. Patient Registration: Register new patients into the system and enter a visit record. Patients start in the primary waiting room.
3. Display information about a specified patient.
4. Room Assignment: Assign patients to specific rooms, reflecting waiting, exam, or procedure rooms.
5. Staff-Patient Assignment: Assign clinical staff members to a patient for care.
6. Unassign a selected clinical staff member from a patient.
7. Send Patient Home: Send a patient home, which is approved by a clinical staff member.

The following features are only available in the text-based controller:

1. Register an existing patient and enter a visit record.
2. List dormant patients who have been in the clinic before but have not been for more than 365 days from today.
3. List frequent patients who have two or more visits in the past 365 days.
4. Register a new clinical staff member.
5. Deactivate a clinical staff member when no longer active.
6. List clinical staff members who have a patient in the clinic, and list currently assigned patients for each staff member.
7. List all the clinical staff members, and for each member, list the number of patients that they have ever been assigned to.
8. Display information about a specified room and its contents.
9. Seating Chart: Display an updated seating chart of all clinic rooms and occupant patients.
10. Create a map of the clinic with the names of the patients in each room.


### How to Run

**From JAR:**

1. Please execute the following command from the command line:

            java -jar clinic.jar <clinic specification file> 

     Replace <clinic specification file> with the path to the text file containing the clinic specifications. This will load clinic data into the model and show the program GUI.
     For example: 
          
            java -jar clinic.jar clinicfile.txt
            
     Or, 
     
            java -jar clinic.jar
            
     Running JAR without specifying the specification file will show the program GUI with empty model data. The user can choose "Load clinic text file" from the system menu to load desired clinic specifications. 

**From code:**
    
1. This project contains a driver class ClinicDriver with main() that demonstrates how to use the functionalities of this clinic application. You can create your own driver as well.
2. Compile the Java source files using Java compiler (javac).
3. Run the driver class from the command line, specifying the specification file in the argument:

            java ClinicDriver <clinic specification file>
            
     Replace <clinic specification file> with the path to the text file containing the clinic specifications. This will load clinic data into the model and show the program GUI.
     For example: 
          
            java ClinicDriver clinicfile.txt 
            
     Or, 
     
            java ClinicDriver 
            
     This will show the program GUI with empty model data. The user can choose "Load clinic text file" from the system menu to load desired clinic specifications. 


### How to Use the Program

The clinic specification file to be read in to populate the data in clinic model must follow the following rules:

1. The file must be in plain text. It contains the name of the clinic and four blocks for rooms, staff, patients, and visit records.
2. The first line of the file is the name of our clinic. 
3. The second line is the number of rooms in the clinic. 
4. The following lines defines a room in each line. Each line must contain exactly these components, and components are separated by one or more spaces.
     - The first two numbers are the X and Y coordinates of the lower-left corner;
     - The next two numbers are the X and Y coordinates of the upper-right corner;
     - The room type (only three types of rooms: waiting, exam, procedure);
     - The name of the room. 
5. The first room in the list is our primary waiting room, therefore the first room must be of type WAITING.
6. After the list of rooms is a line that contains a number that represents the number of people on the staff.
7. The following lines defines a staff member in each line. Staff jobs are divided into clinical staff and non-clinical staff. 
     - The job title (only three types of job title: physician, nurse, reception);
     - The first name;
     - The last name;
     - The education level (only three types of education level: doctoral, masters, allied);
     - The National Provider Identifier (a 10-digit number) for clinical staff or the CPR level for non-clinical staff (only four types of CPR level: A, B, C, BLS).
8. After the list of staff members is a line that contains a number that represents the number of patients in the clinic. 
9. The following lines defines a patient in clinic in each line. 
     - The room number where the patient is assigned to. The room number references the rooms in the order that they are in the file, with the first room identified as 1, and room 0 means not in clinic;
     - The first name;
     - The last name;
     - The date of birth in the format of yyyy/MM/dd, e.g. 2000/12/25.
10. After the list of patients is a line that contains a number that represents the number of visit records in the clinic. 
11. The following lines defines a visit record in each line. 
     - The patient ID;
     - The visit date and time in the format of yyyy/MM/dd HH:mm, e.g. 2000/12/25 15:30;
     - The body temperature in °C;
     - The chief complaint.
     

### Example Runs

The example run is described in the document 'Example run.pdf' located in the '/res' folder. This document contains the description and screenshots demonstrating all functionalities of the GUI-based MVC design of the health clinic application. 

The content of the clinic specification text file 'clinicfile.txt' used in the example run is described as follows. Additionally, the clinic map image file, 'clinic_map.jpg,' is also available in the folder '/res'.

Family Care Clinic
7
0  0 35 60 waiting Front Waiting Room
40 0 54 25 exam Vitality Room
55 0 69 25 exam Wellness Room
70 0 84 25 exam Exam3 Room
85 0 100 25 exam Exam4 Room
40 30 69 60 procedure Operations Room
70 30 100 60 procedure Treatment Room
8
physician Amy Adams doctoral 1234567890
physician Benny Beatrice doctoral 2345678901
physician Camila Crisis doctoral 3456789012
physician Denise Davidson doctoral 4567890123
nurse Emily Emerson doctoral 5678901234
nurse Fiona Francis masters 6789012345
nurse George Gertrude masters 7890123456
reception Irene Isaacson allied B
8
0 Arthur Alfredsson 1981/01/01
1 Betty Baker 1982/02/02
0 Clive Cardiac 1983/03/03
2 Doug Dermo 1984/04/04
0 Elise Enzyme 1985/05/05
3 Fatima Follicle 1986/06/06
0 Greg Gastric 1987/07/07
0 Henry Haralson 1988/08/08
14
1 2021/01/01 10:00 37.1 Chest pain
1 2022/01/01 10:00 37.1 Chest pain
2 2022/02/02 10:00 37.1 Chest pain
2 2024/11/01 10:00 37.1 Chest pain
3 2022/03/03 10:00 37.1 Chest pain
4 2024/04/04 10:00 37.1 Chest pain
4 2024/11/01 10:00 37.1 Chest pain
5 2022/05/05 10:00 37.1 Chest pain
5 2023/05/05 10:00 37.1 Chest pain
6 2022/06/06 10:00 37.1 Chest pain
6 2023/06/06 10:00 37.1 Chest pain
6 2024/06/06 10:00 37.1 Chest pain
6 2024/11/01 10:00 37.1 Chest pain
7 2024/07/07 10:00 37.1 Chest pain


### Design/Model Changes

The program has undergone the following design changes:

Version 1: 
- Initially, the model consisted of classes like Clinic, Room, (abstract) AbstractPerson, (abstract) AbstractStaff, ClinicalStaff, NonClinicalStaff, and Patient. 
- Relationships between classes were maintained through fields in both ends of the relationship (e.g., a field 'assignedPatients: List<Patient>' in Room and a field 'assignedRoom: Room' in Patient).
- I decided to avoid splitting room types into separate classes, which helped keep the model more straightforward without losing clarity. One single class Room with a field 'roomType: RoomType' (Enum EXAM, PROCEDURE, WAITING) can distinguish different room types easily. However, for the design for staff, I decided to implement separate classes for ClinicalStaff and NonClinicalStaff, since the assignment instruction implies that clinical staff do very different things that nonclinical staff. 

Version 2: 
- The abstract class AbstractPerson was changed to an interface Person, implemented by classes (abstract) AbstractStaff and Patient.
- Relationships were simplified by removing redundant fields to maintain relationship at only one end. For example, the field 'assignedPatients: List<Patient>' was removed from Room while keeping 'assignedRoom: Room' in Patient. This information is still retrievable from both sides of the relationship. When judging from which of the two ends of a relationship to remove the redundant field, I choose to remove the field with List type to simplify even more the design and the coding.
- Some private helper methods were added to encapsulate common or repetitive tasks and facilitate the public behavior methods. 

Version 3: 
- An interface ClinicService was added, and the Clinic class was made to implement it. This change promotes better organization and separation of concerns, and facilitates future extension of functionalities. New functionalities can be added by implementing new classes extending existing ones.
- Removed enumeration of JobTitle, should not be limited by existing values physician, nurse, reception and should be open to future changes.

Version 4:
- Removed the interface Person and made the abstract class AbstractStaff to class Staff to simply the model, since there are too little information contained in Person (just two getters getFirstName() and getLastName()).

Version 5:
- Added a class PatientVisitRecord to represent a record for a patient's visit to the clinic. This record includes the date and time of registration, the chief complaint, and the patient's body temperature in degrees Celsius. The relationship between Patient and PatientVisitRecord is an one-to-many (1-*) composition dependency.

Version 6:
- Added interface RoomModel for class Room to serve as a blueprint and provides better level of abstraction and polymorphism.
- Added interface Person for abstract class Staff, which is extended by classes ClinicalStaff and NonClinicalStaff.
- Added interface PatientModel, extending interface Person as well, implemented by class Patient.
- Added class PatientVisitRecord to model the requirements of adding visit records to patients.
- Change class-type variables to interface-type variables for better abstraction and polymorphism.

Version 7:
- Created interface ClinicControllerInterface which is implemented by class ClinicController. This represents a Controller for Health Clinic to handle user commands by executing them using the model and convey the outcomes to the user.
- Created interface ClinicCommand which is implemented by several command classes (e.g. DisplayPatient, DisplayRoom, etc). It uses the command design pattern to unify different sets of operations under one umbrella, so that they can be treated uniformly.
- Created a class RandomGenerator for testing the patient's body temperature and the date in visit records. 

Version 8:
- Modified clinic model based on the milestone 1 manual grading analysis.
- Added RoomInterface, StaffInterface, PatientInterface, and VisitRecordInterface interfaces, and renamed ClinicService interface to ClinicInterface to keep consistent naming.
- Added missing signatures of public methods into interfaces to honor the contract. 
- Modified parameters and returned values of concrete class types to interface types.
- Revised code to return copies to keep the original state and data from external modifications.

Version 9:
- Changes for milestone 3.
- Modified ClinicController to move out the Map initialization of known commands to a new utility KnownCommands class, so the ClinicController can be dedicated to controller function and won't need to be revised for adding/removing commands in the future.
- Created a new Utilities class to provide static shared/common helper methods for all command classes.

Version 10:
- In addition to currently assigned patients, added a field in AbstractStaff to keep history of ever assigned patients.

Version 11:
- Created the SeatingMap class with static drawMap() method to generate the image of clinic with patients in GUI and outputs an image file.

Version 12:
- For the GUI-based controller in Milestone 4, there is no any changes made to the model from Milestone 3 because the model was designed to be independent of the controller and view implementation, whether text-based or GUI-based. This separation of concerns ensures that the model remains focused solely on encapsulating the application's core business logic and data management. By decoupling the model from specific user interface details, it allows for flexibility in swapping or modifying the controller and view without impacting the integrity or functionality of the model. 
- The controller (Controller) is changed to be a singleton like the clinic model (Clinic) with hidden constructor to ensure a sole instance. It still uses the command design pattern as in Milestone 3. All commands need implement the CommandInterface and perform the desired functionalities with execute() method.
- Added the design for the view. The view (ClinicView) is also designed as a singleton and implements ClinicViewInterface. It contains a ClinicPanel which implements ClinicPanelInterface.

Version 13:
- New command classes are created to implement the CommandInterface. Each command class represents a feature in the GUI-based application. The commands that require to show a new window will extend JFrame and uses JPanel. 
- Created a new MenuListener which implements ActionListener to handle the menu clicks and execute corresponding commands.


### Assumptions

- The clinic specification file is formatted correctly and adheres to the expected structure.
- All data in the clinic specification file is valid (e.g., no missing information or invalid values).
- The clinic specification file contains all necessary information to populate the clinic model.
- Staff jobs are divided into clinical staff and non-clinical staff, based on the fact that clinical staff have National Provider Identifiers (NPI), while non-clinical staff have CPR levels.
- Once registered, the name of the clinic, a room, a patient, and a staff member will not be changed. Similarly, the coordinates and type of a room and the date of birth of a patient will not be changed after instantiation. Therefore, no public setters are provided for these fields. 
- A new patient will be assigned to the primary waiting room (room 1) upon registration with a visit record. A referral patient will not be assigned to any room upon registration without a visit record. 
- A new staff member will be in active status upon registration.
- A clinical staff member (either physician or nurse) can be assigned to a patient, but only a physician can send patient home.
- Multiple clinical staff members can be assigned to a single patient at the same time.
- A clinical staff member can be assigned to multiple patients at the same time.
- When listing the staff members and the patients ever assigned to each staff member, deactivated and non-clinical staff members are not included.


### Limitations

- This model assumes a simple clinic structure and may need adjustments for complex scenarios. Additional features may be required for a more comprehensive clinic management system and shall be added to the model.
- There is minimal validation of inputs based on the assumption that the clinic specification file is well-formatted and contains all necessary information. Missing or incorrect information may lead to unexpected results.
- The program does not save state across runs; all data is reset each time the program is run.


### Citations

- A Visual Guide to Layout Managers. (n.d.). Oracle. https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
- How to Use File Choosers. (n.d.). Oracle. https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
- GridBagLayout. (n.d.). O'Reilly Media. https://www.oreilly.com/library/view/learning-java-4th/9781449372477/ch19s06.html
- Java JMenuBar, JMenu and JMenuItem. (n.d.). JavaPoint. https://www.javatpoint.com/java-jmenuitem-and-jmenu
- Class JOptionPane. (n.d.). Oracle. https://docs.oracle.com/javase/8/docs/api/javax/swing/JOptionPane.html
- Nguyen M. (n.d.). Using JButton in JTable. WordPress. https://softwaredesign.home.blog/tutorials/using-jbutton-in-jtable/
- Raja. (2020, February 10). How can we add/insert a JButton to JTable cell in Java? TutorialsPoint. https://www.tutorialspoint.com/how-can-we-add-insert-a-jbutton-to-jtable-cell-in-java
- Goel E. (2022, May 1). SwingWorker in Java. GeeksForGeeks. https://www.geeksforgeeks.org/swingworker-in-java/
- Nishadha. (2022, November 25). UML Class Diagram Relationships Explained with Examples. Creatly. https://creately.com/guides/class-diagram-relationships/
- Model–view–controller. (2024, September 10). Wikipedia. https://en.wikipedia.org/wiki/Model-view-controller
- Interface Readable. (n.d.). Oracle. https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Readable.html
- Class FileReader. (n.d.). Oracle. https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/FileReader.html
- Pankaj and Kouchi, B. (2022, November 30). How To Read a File Line-By-Line in Java. DigitalOcean. https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
- Sruthy. (2024, March 7). List Of JUnit Annotations: JUnit 4 Vs JUnit 5. Software Testing Help. https://www.softwaretestinghelp.com/junit-annotations-tutorial/
- Gilmore K. (2024, January 8). The Command Pattern in Java. Baeldung. https://www.baeldung.com/java-command-pattern
- Pankaj. (2022, August 4). Command Design Pattern. DigitalOcean. https://www.digitalocean.com/community/tutorials/command-design-pattern
