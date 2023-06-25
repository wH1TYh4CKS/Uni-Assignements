# Load the readxl package
library(readxl)
library(ggplot2)
library(dplyr)

# Set the working directory to where the Excel file is located
setwd("C:\\Users\\Asus Rog Hero\\Documents")


# Read the CSV file
placement_data <- read.csv("Placement_Data_Full_Class.csv")

# Change the column names
colnames(placement_data) <- c("Serial_number", 
                              "Gender", 
                              "Age",
                              "Address",
                              "Mother_Education_Level",
                              "Father_Education_Level",
                              "Mother_job","Father_Job",
                              "Family_support",
                              "Paid_extra_classes",
                              "Extra_Curicular_Activities",
                              "Access_Internet",
                              "Secondary_Edu_Percentage", 
                              "Secondary_Board",
                              "Higher_Secondary_Percentage", 
                              "Higher_Secondary_Board", 
                              "Higher_Specialisation", 
                              "Degree_Percentage", 
                              "Degree_Type", 
                              "Work_Exp", 
                              "Employbility_Test_Percentage", 
                              "Specialisation", 
                              "MBA_Percentage", 
                              "Status", 
                              "Salary")

# Verify the new column names
names(placement_data)

#view data
View(placement_data)

#Get summary
summary(placement_data)

#ANALYSIS
  
#Q1: Age vs Staus, Does the Student age have any impact to their job placement status?

#Find out the total number of students in all age group status.(HISTOGRAM)
ggplot(placement_data, aes(x = factor(Age), fill = Status)) + 
  geom_bar(position = "dodge") +
  labs(title = "Age vs. Placement Status",
       x = "Age",
       y = "Number of Students",
       fill = "Status") +
  theme_bw() + 
  theme(plot.title = element_text(hjust = 0.5),
        axis.text.x = element_text(angle = 0, hjust = 0.5),
        legend.position = "bottom") +
  scale_x_discrete(labels = c("18", "19", "20", "21", "22", "23")) +
  geom_text(aes(label=..count.., y=..count..), stat="count", position=position_dodge(width=0.9), size = 3, fontface = "bold")

#To finalize perform t-test
t.test(Age ~ Status, data = placement_data)



#Q2:Does student's gender affect their job placement status?
#perform chiq-test to get a clearer picture
chisq.test(table(placement_data$Gender, placement_data$Status))

#finding the number of male/female that got place/not placed
table(placement_data$Gender, placement_data$Status)

#bar plot to show the findings
ggplot(data = placement_data, aes(x = Gender, fill = Status)) +
  geom_bar(position = "stack") +
  scale_fill_manual(values = c("#0072B2", "#E69F00")) +
  labs(title = "Job Placement by Gender",
       x = "Gender",
       y = "Percentage",
       fill = "Placement Status") +
  theme_bw() +
  theme(plot.title = element_text(hjust = 0.5),
        axis.text.x = element_text(angle = 0, hjust = 0.5),
        legend.position = "bottom")



#Q3:Does Parents support help Students get a job placement
# create a table of counts for each combination of gender, family support, and placement status
gender_support_table <- table(placement_data$Gender, placement_data$Family_support, placement_data$Status)
gender_support_table

# convert the table to a data frame for plotting
gender_support_df <- as.data.frame(gender_support_table)
names(gender_support_df) <- c("Gender", "Family_support", "Status", "Count")
 
# create a stacked bar chart
ggplot(gender_support_df, aes(x = Family_support, y = Count, fill = Status)) +
  facet_wrap(~Gender, ncol = 2) +
  geom_bar(stat = "identity") +
  labs(x = "Family Support", y = "Count", fill = "Placement Status")


#Q4:Does student address affect Student getting a job placement?
# Create a contingency table of address and placement
address_placement_table <- table(placement_data$Address, placement_data$Status)

# Perform chi-squared test
chisq.test(address_placement_table)

# Creating a table of count data
address_table <- table(placement_data$Address, placement_data$Status)

# Converting the table to a data frame
address_df <- as.data.frame.matrix(address_table)

# Renaming the columns for readability
colnames(address_df) <- c("Not Placed", "Placed")

# Adding a column for the total count of each address
address_df$total <- rowSums(address_df)

# Calculating the percentage of placed students for each address
address_df$placed_percent <- address_df$Placed / address_df$total

# Adding confidence intervals
address_df$CI <- qnorm(0.975) * sqrt((address_df$placed_percent * (1 - address_df$placed_percent)) / address_df$total)
address_df
# Creating a bar plot with error bars and labels
ggplot(address_df, aes(x = rownames(address_df), y = placed_percent)) +
  geom_bar(stat = "identity", fill = "steelblue") +
  geom_errorbar(aes(ymin = placed_percent - CI, ymax = placed_percent + CI), width = 0.2) +
  labs(x = "Address", y = "Percent of placed students") +
  ggtitle("Job Placement by Address") +
  geom_text(aes(label = paste0("Placed: ", Placed, "\nNot Placed: ", `Not Placed`)), vjust = -1, size = 3.5)



#Q5: Does student address affect students having access to internet?
# Create a data frame with counts of students by address and internet access
address_internet_counts <- placement_data %>%
  group_by(Address, Access_Internet) %>%
  summarize(count = n())

# Create the bar plot
ggplot(address_internet_counts, aes(x = Address, y = count, fill = Access_Internet)) +
  geom_col(position = "dodge") +
  labs(title = "Count of Students by Address and Internet Access",
       x = "Address",
       y = "Count") +
  theme_minimal()

#Q6: Does access to Internet affects students’ educational percentage?(Secondary, Higher Secondary, Degree and MBA)
# Create separate data frames for students with and without internet access
placed_students <- placement_data[placement_data$Status == "Placed",]
students_with_internet <- placed_students[placement_data$Access_Internet == "yes", ]
students_without_internet <- placed_students[placement_data$Access_Internet == "no", ]

# Plot the graph for students with internet access
ggplot(students_with_internet, aes(x = Secondary_Edu_Percentage, y = ..count.., color = "Secondary")) +
  geom_line(stat = "bin", binwidth = 1) +
  geom_line(aes(x = Higher_Secondary_Percentage, color = "Higher Secondary"), stat = "bin", binwidth = 1) +
  geom_line(aes(x = Degree_Percentage, color = "Degree"), stat = "bin", binwidth = 1) +
  geom_line(aes(x = MBA_Percentage, color = "MBA"), stat = "bin", binwidth = 1) +
  labs(x = "Percentage", y = "Number of Students", title = "Effect of Having Internet Access on Placed Students Education Level") +
  scale_color_manual("", values = c("Secondary" = "red", "Higher Secondary" = "green", "Degree" = "blue", "MBA" = "purple"))

# Plot the graph for students without internet access
ggplot(students_without_internet, aes(x = Secondary_Edu_Percentage, y = ..count.., color = "Secondary")) +
  geom_line(stat = "bin", binwidth = 1) +
  geom_line(aes(x = Higher_Secondary_Percentage, color = "Higher Secondary"), stat = "bin", binwidth = 1) +
  geom_line(aes(x = Degree_Percentage, color = "Degree"), stat = "bin", binwidth = 1) +
  geom_line(aes(x = MBA_Percentage, color = "MBA"), stat = "bin", binwidth = 1) +
  labs(x = "Percentage", y = "Number of Students", title = "Effect of Not Having Internet Access on Placed Students Education Level") +
  scale_color_manual("", values = c("Secondary" = "red", "Higher Secondary" = "green", "Degree" = "blue", "MBA" = "purple"))



#Q7:How does students grades affect students job placement.
#Q7-A1: Secondary Education percentage affect
#create a scatter plot to show.
ggplot(placement_data, aes(x = Status , y = Secondary_Edu_Percentage, color = Secondary_Board)) +
  geom_point(alpha = 0.7) +
  labs(x = "Job Placement Status", y = "Secondary Education Percentage", title = "Relationship between Secondary Education Percentage and Job placement") +
  theme_minimal()

#Q7-A2: Higher Secondary Education percentage affect
#Combined all 3 higher Secondary data to Job placement
# create a data frame with the relevant columns
Higher_Secondary_df <- data.frame(higher_secondary_perc = placement_data$Higher_Secondary_Percentage,
                                  board = placement_data$Higher_Secondary_Board,
                                  specialisation = placement_data$Higher_Specialisation,
                                  placement =placement_data$Status)
# convert placement column to a factor
Higher_Secondary_df$placement <- factor(Higher_Secondary_df$placement, levels = c("Not Placed", "Placed"))

# group by higher_secondary_perc, board, and specialization, and count the number of placements/non-placements
HS_count_df <- aggregate(Higher_Secondary_df$placement, 
                         by = list(Higher_Secondary_df$higher_secondary_perc, Higher_Secondary_df$board, Higher_Secondary_df$specialisation,
                                   Higher_Secondary_df$placement), 
                         FUN = length)
names(HS_count_df) <- c("higher_secondary_perc", "board", "specialisation", "placement", "count")

#Create the bar plot
ggplot(HS_count_df, aes(x=higher_secondary_perc, y=count, fill=placement)) + 
  geom_bar(stat="identity", position="dodge") + 
  facet_grid(specialisation ~ board) + 
  labs(x="Higher Secondary Percentage", y="Count", fill="Placement Status") + 
  theme_bw()

# Perform ANOVA
result <- aov(Higher_Secondary_Percentage ~ Higher_Secondary_Board, data = placement_data)

# Print ANOVA table
summary(result)

#Q7-A3:Students degree percentage and specialization 
ggplot(placement_data, aes(x = Degree_Percentage, fill = Status)) +
  geom_histogram(alpha = 0.7, position = position_dodge(width = 1.5), bins = 20) +
  facet_wrap(~Degree_Type) +
  labs(x = "Degree Percentage", y = "Count", 
       title = "Distribution of Degree Percentage by Job Placement and Type") +
  theme_minimal()


#Q7-A4 Masters Percentage and Specialisation benefits for job placement
ggplot(placement_data, aes(x=MBA_Percentage, y=..count.., group=Status, color=Status)) +
  geom_line(stat="bin", binwidth=5) +
  facet_wrap(~Specialisation) +
  labs(title="Number of Students by Specialisation and MBA Percentage", x="MBA Percentage", y="Number of Students") +
  theme_minimal()



#Q8:Does having extra classes help the student in their job placement status?
# Re-code Status as binary variable
placement_data$Status_binary <- ifelse(placement_data$Status == "Placed", 1, 0)
placement_data$Paid_extra_classes_binary<- ifelse(placement_data$Paid_extra_classes=="yes",1,0)
# Perform logistic regression analysis
logit_model <- glm(Status_binary ~ Paid_extra_classes_binary, data = placement_data, family = binomial(link = "logit"))

# Summarize the model results
summary(logit_model)

# Creating a sequence of extra classes values for prediction
extra_classes_seq <- seq(from = min(placement_data$Paid_extra_classes_binary), to = max(placement_data$Paid_extra_classes_binary), by = 0.1)

# Making a dataframe of the sequence
extra_classes_df <- data.frame(Paid_extra_classes_binary = extra_classes_seq)

# Adding predicted probabilities to the dataframe
extra_classes_df$Placed <- predict(logit_model, newdata = extra_classes_df, type = "response")

# Creating a plot of the predicted probabilities
ggplot(extra_classes_df, aes(x = Paid_extra_classes_binary, y = Placed)) +
  geom_line() +
  ggtitle("Probability of Being Placed Based on Extra Classes Taken") +
  labs(x = "Extra Classes Taken", y = "Probability of Being Placed")



#Q9: Do parents education level affect student chances of a job placement?
# Create a new variable that sums the father and mother education levels
placement_data$Combined_Edu_Level <- placement_data$Father_Education_Level + placement_data$Mother_Education_Level

# Create a contingency table of combined education level and placement status
combined_edu_table <- table(placement_data$Combined_Edu_Level, placement_data$Status)
combined_edu_table
combined_edu_df<-as.data.frame(combined_edu_table)
# Create a bar plot of combined education level and placement status
ggplot(combined_edu_df, aes(x=Var1, y=Freq, fill=Var2)) +
  geom_bar(stat="identity", position="dodge") +
  labs(title="Placement by Parent's Combined Education Level", x="Total Education Level", y="Number of Students") +
  theme(plot.title = element_text(hjust = 0.5)) +
  scale_fill_manual(name = "Placement Status", labels = c("Not Placed", "Placed"), values = c("#E69F00", "#56B4E9")) +
  theme(legend.position = "bottom")

#Q9-A1: Do fathers have more affect?
# Create a contingency table of father's education level and placement status
father_edu_table <- table(placement_data$Father_Education_Level, placement_data$Status)
father_edu_table
 
father_edu_df <- as.data.frame(father_edu_table)

ggplot(father_edu_df, aes(x=Var1, y=Freq, fill=Var2)) +
  geom_bar(stat="identity", position="dodge") +
  labs(title="Placement by Father's Education Level", x="Father's Education Level", y="Number of Students") +
  theme(plot.title = element_text(hjust = 0.5)) +
  scale_fill_manual(name = "Placement Status", labels = c("Not Placed", "Placed"), values = c("#E69F00", "#56B4E9")) +
  theme(legend.position = "bottom")

#Q9-A2: Do mothers have more affect?
# Create a contingency table of mother's education level and placement status
mother_edu_table <- table(placement_data$Mother_Education_Level, placement_data$Status)
mother_edu_table

mother_edu_df <- as.data.frame(mother_edu_table)

ggplot(mother_edu_df, aes(x=Var1, y=Freq, fill=Var2)) +
  geom_bar(stat="identity", position="dodge") +
  labs(title="Placement by Mother's Education Level", x="Mother's Education Level", y="Number of Students") +
  theme(plot.title = element_text(hjust = 0.5)) +
  scale_fill_manual(name = "Placement Status", labels = c("Not Placed", "Placed"), values = c("#E69F00", "#56B4E9")) +
  theme(legend.position = "bottom")


#Q10: Does Parents occupation affect students chances in placing for a job?
#Putting the data into table
father_mother_job_placement_table <- table(placement_data$Father_Job, placement_data$Mother_job, placement_data$Status)
father_mother_job_placement_table

# Convert the table to a data frame
father_mother_job_placement_df <- as.data.frame.table(father_mother_job_placement_table)
names(father_mother_job_placement_df) <- c("Father_Job", "Mother_job", "Status", "Count")

# Create the heat map
ggplot(father_mother_job_placement_df, aes(x = Father_Job, y = Mother_job, fill = Count)) +
  geom_tile(color = "white") +
  scale_fill_gradient(low = "white", high = "steelblue") +
  facet_wrap(~ Status, ncol = 2) +
  labs(x = "Father's Job", y = "Mother's Job", fill = "Count") +
  theme_minimal() +
  theme(axis.text.x = element_text(angle = 90, vjust = 0.5))

#Q11:Extra Curricular Activities affect on Students Job Placement.
# Calculate counts of placed vs not placed based on extra curricular activities
counts <- table(placement_data$Extra_Curicular_Activities, placement_data$Status)
counts_df <- data.frame(counts)
counts_df$Activity <- rownames(counts_df)

# Calculate percentages
counts_df$Percentages <- round(counts_df$Freq / sum(counts_df$Freq) * 100, 2)
counts_df$Percentages
# Plot pie chart
ggplot(counts_df, aes(x="", y=Freq, fill=Activity)) + 
  geom_bar(stat="identity", width=1) + 
  coord_polar("y", start=0) +
  geom_text(aes(label=paste0(Percentages, "%")), position=position_stack(vjust=0.5)) +
  labs(title = "Placement status based on extra curricular activities", fill="Activity") +
  theme_void() +
  theme(legend.position="right")



#Q12: Does having Job experience affect students chances of getting a job?
# create a data frame with the count of students placed and not placed based on their work experience
exp_placement <- placement_data %>%
  group_by(Work_Exp, Status) %>%
  summarize(count = n()) %>%
  mutate(Status = factor(Status, levels = c("Not Placed", "Placed")))

# create the bar chart
ggplot(exp_placement, aes(x = Work_Exp, y = count, fill = Status)) +
  geom_bar(position = "dodge", stat = "identity") +
  labs(title = "Job Placement by Work Experience",
       x = "Work Experience (in years)",
       y = "Number of Students",
       fill = "Placement Status") +
  theme_minimal()

# Create a contingency table
work_exp_placement <- table(placement_data$Work_Exp, placement_data$Status_binary)

# Perform the chi-squared test
chisq.test(work_exp_placement)



#Q13: Does employability test scores tell us students are likely to be placed in Jobs?

ggplot(placement_data, aes(x = Status, y = Employbility_Test_Percentage, fill = Status)) +
  geom_boxplot() +
  ggtitle("Employability Test Scores by Job Placement Status") +
  xlab("Job Placement Status") +
  ylab("Employability Test Scores") +
  theme_minimal() +
  scale_fill_manual(values=c("#00AFBB", "#FC4E07"), name="Status", labels=c("Placed", "Not Placed"))

wilcox.test(Employbility_Test_Percentage ~ Status, data = placement_data)



#Q14: Does students degree, Specialisation and Work experience affect their salary?
placement_data %>%
  group_by(Degree_Type, Specialisation, Work_Exp) %>%
  summarise(mean_salary = mean(Salary, na.rm = TRUE)) %>%
  ggplot(aes(x = Specialisation, y = mean_salary/1000, fill = Degree_Type)) +
  geom_col(position = "dodge") +
  facet_wrap(vars(Work_Exp)) +
  labs(title = "Mean Salary by Degree Type, Specialisation and Work Experience",
       x = "Specialisation",
       y = "Mean Salary (in thousands)",
       fill = "Degree Type") +
  theme_minimal() +
  scale_y_continuous(labels = scales::comma)






