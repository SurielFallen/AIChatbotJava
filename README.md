SebastAIn is a personal chat bot based off of a locally running LLM. By using a custom prompt input file, users can define the AI's personality and purpose. 
The project uses Ollama for pulling and running a model, along with Ollama4j to interface with the running model.

![DesignDoc](https://github.com/user-attachments/assets/259c6505-536f-44c1-8c1d-8ab8c14742c1)

**This project requires a locally running llm active in ollama during execution. It does NOT connect to an API**

If you aren't using mistral, you can specify the model name on line 31 of the LLM class. 


**Current functionality:**
1. Console input/output chat
2. Selectable prompt input via files located in "resources"
   

**Areas of improvement:**

Currently, the entire chat history is stored and sent via string. There may be more optomized ways to interface with the llm and store chat histories, either via Ollama4j or some other method. This also relates to crashing errors occuring after long conversations. 


**This project is no longer updated**

This project has been used to interface with an LLM while using a language I'm most familiar with, Java. I'm currently leaving this project as-is in order to create a more extenive AI chat bot in python. 
