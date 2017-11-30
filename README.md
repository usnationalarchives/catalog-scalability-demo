# catalog-scalability-demo
## Purpose of the Demo
NARA is facing an ever-increasing volume of archival descriptions and digital objects in the National Archives Catalog and its staff data entry and database system, the Description and Authority Service (DAS). The current system design for DAS using Oracle 12c running on single-node first-generation AWS instances as a data store cannot support the anticipated growth in archival descriptions and digital objects. While current National Archives Catalog search servers can scale horizontally to support such growth, it is dependent on a weekly export of structured XML data from DAS to be ingested by its content processing module and indexed in its Apache Solr-based search engine before descriptions and objects are publicly discoverable.

Therefore, NARA developed this demo to redesign DAS as a scalable application that allows the agency to not only meet but exceed their existing production quotas for providing archival descriptions and digital object and establish tighter integration between DAS and the National Archives Catalog so that records are available for the public to view as soon as they are approved in DAS. NARA will design a new, horizontally-scalable data store that is shared between DAS and the Catalog and a new search cluster that can service both systems. The shared data store and search engine approach eliminates the need for weekly DAS exports. Instead, the indexing of the data can happen directly in DAS, and the Catalog APIs can be modified to present this DAS data to public users.

This demo is a draft re-design of DAS and the Catalog that provides actionable guidance for follow-on modernization efforts to build a system that can keep up with the ever-increasing flow of federal records to NARA and scale reliably (without affecting performance) to support billions of archival descriptions and digital objects in the system by the end of this decade.

## System Architecture Changes
For more details please read the [System Architecture Document (SAD)](https://github.com/usnationalarchives/catalog-scalability-demo/blob/master/TO5_System_Architecture_Doc_v1_0_9_25_2017_Preliminary_Version.docx).
### Current Architecture
#### Description and Authority Service (DAS)
![DAS current architecture](https://github.com/usnationalarchives/catalog-scalability-demo/blob/master/architecture/dascurrent.png)
#### National Archives Catalog
![Catalog current architecture](https://github.com/usnationalarchives/catalog-scalability-demo/blob/master/architecture/catalogcurrent.png)
### Future Architecture
#### Description and Authority Service (DAS)
![DAS future architecture](https://github.com/usnationalarchives/catalog-scalability-demo/blob/master/architecture/dasfuture.png)
#### National Archives Catalog
![Catalog future architecture](https://github.com/usnationalarchives/catalog-scalability-demo/blob/master/architecture/catalogfuture.png)
#### Combined DAS-Catalog Architecture
![Comebined DAS-Catalog architecture](https://github.com/usnationalarchives/catalog-scalability-demo/blob/master/architecture/dascatalogarchitecture.png)
