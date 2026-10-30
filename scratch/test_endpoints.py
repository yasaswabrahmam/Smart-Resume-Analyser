import urllib.request
import urllib.parse
import re
import http.cookiejar

cookie_jar = http.cookiejar.CookieJar()
opener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cookie_jar))

# 1. Fetch Explore page to retrieve CSRF token
page = opener.open('http://localhost:8085/explore').read().decode('utf-8')
csrf_matches = re.findall(r'name="_csrf"\s+value="([^"]+)"', page)
csrf = csrf_matches[0] if csrf_matches else ''
print("CSRF Token retrieved:", bool(csrf))

# 2. Submit paste JD to /explore/infer-role
payload = urllib.parse.urlencode({
    'targetTitle': 'Robotics Vision Engineer',
    'jobDescription': 'We require strong Python, C++, Docker, Linux, and Computer Vision for autonomous robotics navigation.',
    '_csrf': csrf
}).encode('utf-8')

req = urllib.request.Request('http://localhost:8085/explore/infer-role', data=payload)
res = opener.open(req)
print("Redirected URL:", res.geturl())
print("HTTP Status:", res.status)

redirect_content = res.read().decode('utf-8')
print("Contains Inferred Banner:", "Custom Role Profile Inferred Successfully!" in redirect_content)
print("Contains Robotics Vision Engineer:", "Robotics Vision Engineer" in redirect_content)

# 3. Test REST API /explore/api/role/Full%20Stack%20Developer
role_res = opener.open('http://localhost:8085/explore/api/role/Full%20Stack%20Developer')
print("REST API Status:", role_res.status)
role_json = role_res.read().decode('utf-8')
print("REST API Role Name present:", '"roleName":"Full Stack Developer"' in role_json)
