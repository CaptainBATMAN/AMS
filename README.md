## Gmeet Attendance Tracker 
### _A tool to keep track of the Gmeet audit logs from Google workspace._

_This project is to help people in maintaining their Gmeet audit logs._

### How To Use:

#### Step-1 : Deployment
##### To deploy on a local server in organisation's intranet.
- Install Apache Tomcat.
- Browse into ```your-tomcat-installation-directory/conf/```
- Now edit tomcat-users.xml and add credentials for manager and admin roles. 
>- If using linux, make sure to have the root previliges to edit the file.
- Create manager and admin credentials in deploy the WAR Package (file with .war extension) available in /target folder.
- Your url will be ```[your-local-ip]:[tomcat-port]```
  
