path = 'Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/SecurityConfig.java'
with open(path, 'r', encoding='utf-8') as f:
    text = f.read()

target = '"/explore/**",'
replacement = '"/explore/**", "/copilot/**",'
if target in text:
    new_text = text.replace(target, replacement)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(new_text)
    print('Updated SecurityConfig successfully')
else:
    print('Target not found in SecurityConfig')
