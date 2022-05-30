# MediRec
A medical image reconstruction platform (an undergraduate 
graduation project).

Now it's under development and probably not available to use 
due to the developer's limited coding ability...

## Framework in use
- Backend: Spring Boot + MyBatis-Plus
- Frontend: Bootstrap + Bootstrap Table
- Database: MySQL

## Underlying algorithm
FBPConvNet, proposed by Jin et al. in 2016, is a deep learning 
method based on CNN. The algorithm as the underlying processing 
approach for input CT images is implemented via Pytorch. And the
project directly calls the model parameters that have been trained 
and saved. But the model parameter files are not uploaded due to 
GitHub's file size limitation.

## Copyright Statement
This project is possibly incapable of being deployed right now 
for it's only tested under develop mode and it lacks multiple functions. 
So it's not recommended being used.
