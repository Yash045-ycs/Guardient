import os
import mediapipe as mp

from mediapipe.tasks.python.genai import llm_inference

current_dir = os.getcwd()
model_files = [f for f in os.listdir(current_dir) if f.endswith('.task') or f.endswith('.bin')]

if not model_files:
    print(f"❌ Error: No .task or .bin model file found in: {current_dir}")
    print("Please ensure your Gemma model file is copied into this Guardient folder.")
    exit()

model_path = os.path.join(current_dir, model_files[0])
print(f"Found model file: {model_files[0]}")
print("Loading model runtime (this may take a few seconds)...")

try:
    options = llm_inference.LlmInferenceOptions(
        model_path=model_path,
        max_tokens=256,
        temperature=0.2
    )
    
    with llm_inference.LlmInference.create_from_options(options) as llm:
        print("\n✅ SUCCESS: Model loaded into memory successfully!")
        
        prompt = "Hello, respond with a short test message."
        print(f"Sending prompt: '{prompt}'")
        
        response = llm.generate_response(prompt)
        print("\nModel Output:")
        print("----------------------------------------")
        print(response)
        print("----------------------------------------")

except Exception as e:
    print("\n❌ FAILURE: Could not load or execute model.")
    print(f"Error details: {e}")