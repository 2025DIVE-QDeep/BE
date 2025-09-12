
# 📝 B&Ive ( Busan I haved ) 


<br>

## 📌 Project Overview
- **Project Period**: July , 4th 2025 ~ September , 10th 2025 <br><br>

- **Purpose**: To build a platform where users share reviews of less-visited places and posts about local spots or services. By giving badges for first-time reviews and ranking users through a score system, the platform fosters friendly competition that encourages exploration of hidden gems. This approach helps reduce regional imbalances and promotes local economic growth and tourism. <br> And more , The badge system can be extended in the future to tangible rewards or products, making the platform applicable as a real-world service and further incentivizing user participation. <br><br>

- **Background**: 
<br>We conducted an EDA (Exploratory Data Analysis) on consumer spending data within Busan, examining patterns by district (gu), gender, and age group, as well as transaction counts and total sales. <br>

<img width="566" height="319" alt="스크린샷 2025-09-12 오후 3 08 04" src="https://github.com/user-attachments/assets/a457f646-5fc7-41f4-b8cc-f5cd2114ad8e" />
<img width="598" height="762" alt="스크린샷 2025-09-12 오후 3 10 33" src="https://github.com/user-attachments/assets/fff3bbb1-df4d-4fb1-b7ef-95a3ceb23f7b" />
<img width="595" height="699" alt="스크린샷 2025-09-12 오후 3 10 39" src="https://github.com/user-attachments/assets/5ffd5f6c-80f0-4255-9972-60ec11549aa0" />
<img width="593" height="783" alt="스크린샷 2025-09-12 오후 3 10 50" src="https://github.com/user-attachments/assets/41b97af7-494f-4b85-9bda-f35fa8748432" />
<img width="608" height="361" alt="스크린샷 2025-09-12 오후 3 16 30" src="https://github.com/user-attachments/assets/e26b4959-aa53-4531-b5a9-4bae4c36eb4b" />
<img width="623" height="702" alt="스크린샷 2025-09-12 오후 3 10 44" src="https://github.com/user-attachments/assets/f2450a33-9613-41f5-be2e-6703b22f14b1" />

<br>
The analysis revealed significant disparities in spending between districts, highlighting an imbalance in local consumption. <br>

https://news.knn.co.kr/news/article/151108 <br>
https://channelpnu.pusan.ac.kr/news/articleView.html?idxno=36707

Further investigation showed that districts with higher spending were often more tourism-oriented, attracting more visitors, while less-visited districts had lower spending.
These insights inspired the design of our platform, aimed at encouraging exploration of under-visited areas to help balance local tourism and economic activity. 

<br>

## 🛠 Tech Stack
### Backend
- Java 21, Spring Boot 3.5.4  
- Spring Security 6 ( Jwt Token ) 
- JPA/Hibernate, MySQL  

### Frontend
- React 18, TypeScript  
- TailwindCSS  

### Infra & DevOps
- AWS EC2, RDS(MySQL), S3  
- AWS IAM (permission management & security policies)  
- Nginx (reverse proxy)  
- GitHub Actions (CI/CD)  
- Redis (caching & token storage)  



## 📐 Architecture
<img width="435" height="569" alt="스크린샷 2025-09-12 오후 2 26 47" src="https://github.com/user-attachments/assets/a96d695e-041f-48c3-99fd-d1be9a0bfcae" />


## 📊 ERD
<img width="998" height="658" alt="스크린샷 2025-09-10 오전 1 00 56" src="https://github.com/user-attachments/assets/311e5c96-d79c-44d1-ba14-d621919dcd47" />

---

## 🚀 Key Features

<br>

- User Authentication/Authorization: JWT-based (Access + Refresh Token structure)

<img width="373" height="665" alt="스크린샷 2025-09-12 오후 3 38 54" src="https://github.com/user-attachments/assets/cff83920-1b35-4937-b6aa-acf51a23cae3" />
<br><br>

- Post Management: Create, update, delete posts with text + image upload (S3 integration)
- Personalized Recommendations: Suggests suitable places and optimal visiting times using OpenAI GPT API and survey data
- Review Posting: Allows users to write reviews for places they have visited <br><br>
<img width="428" height="712" alt="스크린샷 2025-09-12 오후 3 40 15" src="https://github.com/user-attachments/assets/9b293c88-7cb7-4d6d-bf97-3fbe1769d349" />
<img width="428" height="794" alt="스크린샷 2025-09-12 오후 3 41 35" src="https://github.com/user-attachments/assets/cdaae800-47f6-4604-9f58-e6abef3ab679" />
<img width="426" height="790" alt="스크린샷 2025-09-12 오후 3 41 46" src="https://github.com/user-attachments/assets/feb249d7-deb8-4b95-ae00-59a7cb0df440" />
<img width="427" height="800" alt="스크린샷 2025-09-12 오후 3 43 20" src="https://github.com/user-attachments/assets/adf8362f-a34f-461e-b8b1-270a44e3182f" />
<img width="419" height="797" alt="스크린샷 2025-09-12 오후 3 43 07" src="https://github.com/user-attachments/assets/67b1ba8d-ca38-44c2-858a-c38d5958b5be" />
<img width="427" height="789" alt="스크린샷 2025-09-12 오후 3 44 46" src="https://github.com/user-attachments/assets/01500bd4-561d-4fa9-8f6b-039dbe69682a" />

- Ranking System: Displays top 10 users based on scores in ascending order, promoting friendly competition and engagement
<img width="441" height="786" alt="스크린샷 2025-09-12 오후 3 43 29" src="https://github.com/user-attachments/assets/a6c4117b-64bd-408b-883c-60e0fedd2d04" />


---

## 🧩 Troubleshooting

( 수정중 ) 

---

## 🔧 Refactoring & Improvements

( 수정중 ) 

---




