path = 'Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/SecurityConfig.java'
with open(path, 'r', encoding='utf-8') as f:
    text = f.read()

target = 'new AntPathRequestMatcher("/api/**")'
replacement = 'new AntPathRequestMatcher("/api/**"),\n                new AntPathRequestMatcher("/copilot/api/**")'

if target in text:
    new_text = text.replace(target, replacement)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(new_text)
    print("Updated CSRF ignoring in SecurityConfig")
else:
    print("Target not found")
