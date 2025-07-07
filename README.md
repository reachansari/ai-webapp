# AI Webapp (Java, Spring Boot)

A Java-based AI webapp that:

✅ Uploads and splits large PDF technical manuals  
✅ Generates vector embeddings using OpenAI  
✅ Stores embeddings in Milvus vector DB  
✅ Queries weather data via OpenWeather API  
✅ Dark mode Thymeleaf UI  
✅ Multi-city weather search  
✅ Downloadable PDF links  
✅ Advanced logging

---

## ✨ Features

- Upload PDFs and instantly index content
- Automatic chunking and embedding via OpenAI
- Persist chunks and vectors into Milvus
- Search Milvus (future extension)
- Weather lookup for multiple cities
- Dark mode, modern Thymeleaf UI
- Download PDFs from the web UI
- Maven-based project
- Docker support (Milvus recommended in Docker)

---

## 💻 How to Run

### 1. Start Milvus

Spin up Milvus via Docker (simplest way):

```bash
docker run -d --name milvus-standalone \
  -p 19530:19530 -p 9091:9091 \
  milvusdb/milvus:v2.4.0
