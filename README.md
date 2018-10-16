
# Anypoint Template: SFDC2SAP-product-broadcast	

<!-- Header (start) -->

<!-- Header (end) -->

# License Agreement
This template is subject to the conditions of the <a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>. Review the terms of the license before downloading and using this template. You can use this template for free with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio. 
# Use Case
<!-- Use Case (start) -->
This Anypoint template serves as a foundation for setting an online sync of products/materials from Salesforce to SAP.
Everytime there is a new product or a change in already existing one in Salesforce instance, the template will fetch it and upsert it in SAP.
			
Requirements have been set not only to be used as examples, but also to establish a starting point to adapt your integration to your requirements.
			
As implemented, this template leverage the Mule batch module.
The batch job is divided into *Process* and *On Complete* stages.
The integration is triggered by scheduler to Salesforce products. New or modified products with non-empty product code are passed to the batch as input.
In the batch the material is fetched from SAP by its `ProductCode`. Depending on the result, the product is then created or updated in SAP.
Finally during the *On Complete* stage the template logs output statistics data into the console.
<!-- Use Case (end) -->

# Considerations
<!-- Default Considerations (start) -->

<!-- Default Considerations (end) -->

<!-- Considerations (start) -->
To make this template run, there are certain preconditions that must be considered. All of them deal with the preparations in both, that must be made for the template to run smoothly.
**Failing to do so could lead to unexpected behavior of the template.**

Before continue with the use of this template, you may want to check out this [Documentation Page](http://www.mulesoft.org/documentation/display/current/SAP+Connector#SAPConnector-EnablingYourStudioProjectforSAP), that will teach you how to work 
with SAP and Anypoint Studio.

## Disclaimer

This Anypoint template uses a few private Maven dependencies from Mulesoft in order to work. If you intend to run this template with Maven support, you need to add three extra dependencies for SAP to the pom.xml file.
<!-- Considerations (end) -->


## SAP Considerations

Here's what you need to know to get this template to work with SAP.


### As a Data Destination

This template uses custom BAPI functions. To create them please use following steps:

1. Create structure `ZMMST_ENH_MARA` in transaction `SE11` as per its definition in file `structure_ZMMST_ENH_MARA.abap`
2. Create table type `ZMMTTY_ENH_MARA` in transaction `SE11` as per its definition in file `table_type_ZMMTTY_ENH_MARA.abap`
3. Create message class called `ZMULESOFTINTEGRATION` in transaction `SE91` as per definition in file `msg_class_ZMULESOFTINTEGRATION.abap`
4. Create function module `ZMMFM_MATERIAL_GETLIST` in transaction `SE37` as per source file `ZMMFM_MATERIAL_GETLIST.abap`

Referenced files are in [src/main/resources] directory.
## Salesforce Considerations

Here's what you need to know about Salesforce to get this template to work:

- Where can I check that the field configuration for my Salesforce instance is the right one? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US">Salesforce: Checking Field Accessibility for a Particular Field</a>.
- Can I modify the Field Access Settings? How? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US">Salesforce: Modifying Field Access Settings</a>.

### As a Data Source

If the user who configured the template for the source system does not have at least *read only* permissions for the fields that are fetched, then an *InvalidFieldFault* API fault displays.

```
java.lang.RuntimeException: [InvalidFieldFault [ApiQueryFault 
[ApiFault  exceptionCode='INVALID_FIELD'
exceptionMessage='Account.Phone, Account.Rating, Account.RecordTypeId, 
Account.ShippingCity
^
ERROR at Row:1:Column:486
No such column 'RecordTypeId' on entity 'Account'. If you are attempting to 
use a custom field, be sure to append the '__c' after the custom field name. 
Reference your WSDL or the describe call for the appropriate names.'
]
row='1'
column='486'
]
]
```











# Run it!
Simple steps to get this template running.
<!-- Run it (start) -->

<!-- Run it (end) -->

## Running On Premises
In this section we help you run this template on your computer.
<!-- Running on premise (start) -->

<!-- Running on premise (end) -->

### Where to Download Anypoint Studio and the Mule Runtime
If you are new to Mule, download this software:

+ [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
+ [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)

**Note:** Anypoint Studio requires JDK 8.
<!-- Where to download (start) -->

<!-- Where to download (end) -->

### Importing a Template into Studio
In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your Anypoint Platform credentials, search for the template, and click Open.
<!-- Importing into Studio (start) -->

<!-- Importing into Studio (end) -->

### Running on Studio
After you import your template into Anypoint Studio, follow these steps to run it:

+ Locate the properties file `mule.dev.properties`, in src/main/resources.
+ Complete all the properties required as per the examples in the "Properties to Configure" section.
+ Right click the template project folder.
+ Hover your mouse over `Run as`.
+ Click `Mule Application (configure)`.
+ Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`.
+ Click `Run`.
<!-- Running on Studio (start) -->
In order to make this template run on Anypoint Studio there are a few extra steps that need to be made.
Please check this Documentation Page:

+ [Enabling Your Studio Project for SAP](https://docs.mulesoft.com/connectors/sap-connector#configuring-the-connector-in-studio-7)
<!-- Running on Studio (end) -->

### Running on Mule Standalone
Update the properties in one of the property files, for example in mule.prod.properties, and run your app with a corresponding environment variable. In this example, use `mule.env=prod`. 


## Running on CloudHub
When creating your application in CloudHub, go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the mule.env value.
<!-- Running on Cloudhub (start) -->

<!-- Running on Cloudhub (end) -->

### Deploying a Template in CloudHub
In Studio, right click your project name in Package Explorer and select Anypoint Platform > Deploy on CloudHub.
<!-- Deploying on Cloudhub (start) -->

<!-- Deploying on Cloudhub (end) -->

## Properties to Configure
To use this template, configure properties such as credentials, configurations, etc.) in the properties file or in CloudHub from Runtime Manager > Manage Application > Properties. The sections that follow list example values.
### Application Configuration
<!-- Application Configuration (start) -->
+ scheduler.frequency `10000`
+ scheduler.start.delay `5000`

**Watermarking default last query timestamp e.g. 2016-12-13T03:00:59Z**
+ watermark.default.expression `2014-06-26T12:30:00.000Z`

**SAP Connector Configuration**

+ sap.jco.ashost `your.sap.address.com`
+ sap.jco.user `SAP_USER`
+ sap.jco.passwd `SAP_PASS`
+ sap.jco.sysnr `14`
+ sap.jco.client `800`
+ sap.jco.lang `EN`

**SAP Material properties Configuration**

+ sap.material.type `ZHTI`
+ sap.material.industrySector `T`
+ sap.material.baseUnitOfMeasurement `KGS`

**SalesForce Connector Configuration**

+ sfdc.username `bob.dylan@sfdc`
+ sfdc.password `DylanPassword123`
+ sfdc.securityToken `avsfwCUl7apQs56Xq2AKi3X`
<!-- Application Configuration (end) -->

# API Calls
<!-- API Calls (start) -->
Salesforce imposes limits on the number of API Calls that can be made. However, in this template, only one call per poll cycle is done to retrieve all the information required.
<!-- API Calls (end) -->

# Customize It!
This brief guide provides a high level understanding of how this template is built and how you can change it according to your needs. As Mule applications are based on XML files, this page describes the XML files used with this template. More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml<!-- Customize it (start) -->

<!-- Customize it (end) -->

## config.xml
<!-- Default Config XML (start) -->
This file provides the configuration for connectors and configuration properties. Only change this file to make core changes to the connector processing logic. Otherwise, all parameters that can be modified should instead be in a properties file, which is the recommended place to make changes.<!-- Default Config XML (end) -->

<!-- Config XML (start) -->

<!-- Config XML (end) -->

## businessLogic.xml
<!-- Default Business Logic XML (start) -->
Functional aspect of the template is implemented on this XML, directed by a batch job that will be responsible for creations/updates. The several message processors constitute four high level actions that fully implement the logic of this template:
1. The integration is triggered by scheduler to Salesforce Products. New or modified products are passed to the batch as input.
2. In the batch the material is fetched from SAP by its `ProductCode`. Depending on the result, the product is then created or updated in SAP.
3. Finally during the *On Complete* stage the template logs output statistics data into the console.<!-- Default Business Logic XML (end) -->

<!-- Business Logic XML (start) -->

<!-- Business Logic XML (end) -->

## endpoints.xml
<!-- Default Endpoints XML (start) -->
This file is conformed by two Flows.
The first one we'll call it **scheduler** flow. This one contains the Scheduler endpoint that will periodically trigger **query** flow and then executing the batch job process.
The second one we'll call it **query** flow. This one contains watermarking logic that will be querying Salesforce for updated/created Products that meet the defined criteria in the query since the last polling. The last invocation timestamp is stored by using Objectstore Component and updated after each Salesforce query.<!-- Default Endpoints XML (end) -->

<!-- Endpoints XML (start) -->

<!-- Endpoints XML (end) -->

## errorHandling.xml
<!-- Default Error Handling XML (start) -->
This file handles how your integration reacts depending on the different exceptions. This file provides error handling that is referenced by the main flow in the business logic.<!-- Default Error Handling XML (end) -->

<!-- Error Handling XML (start) -->

<!-- Error Handling XML (end) -->

<!-- Extras (start) -->

<!-- Extras (end) -->
