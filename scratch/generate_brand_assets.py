import os
import math
from PIL import Image, ImageDraw, ImageFilter

base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
brand_assets_dir = os.path.join(base_dir, 'brand-assets')
static_brand_dir = os.path.join(base_dir, 'src', 'main', 'resources', 'static', 'images', 'brand')

os.makedirs(brand_assets_dir, exist_ok=True)
os.makedirs(static_brand_dir, exist_ok=True)

svg_content = """<svg class="brand-3d-svg" viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Smart Resume AI Northstar Path">
    <defs>
        <!-- Background Ambient Glow -->
        <radialGradient id="northstarAmbientGlow" cx="50%" cy="50%" r="50%">
            <stop offset="0%" stop-color="#21D2CF" stop-opacity="0.35"/>
            <stop offset="50%" stop-color="#356BFA" stop-opacity="0.15"/>
            <stop offset="100%" stop-color="#071426" stop-opacity="0"/>
        </radialGradient>

        <!-- Base Plate Metallic Gradient -->
        <linearGradient id="northstarPlateGrad" x1="20" y1="20" x2="140" y2="140" gradientUnits="userSpaceOnUse">
            <stop offset="0%" stop-color="#101D31"/>
            <stop offset="50%" stop-color="#0A1728"/>
            <stop offset="100%" stop-color="#071426"/>
        </linearGradient>

        <!-- Hairline Border Gradient -->
        <linearGradient id="northstarBorderGrad" x1="16" y1="16" x2="144" y2="144" gradientUnits="userSpaceOnUse">
            <stop offset="0%" stop-color="#F4C46A" stop-opacity="0.85"/>
            <stop offset="35%" stop-color="#21D2CF" stop-opacity="0.95"/>
            <stop offset="70%" stop-color="#356BFA" stop-opacity="0.75"/>
            <stop offset="100%" stop-color="#101D31" stop-opacity="0.3"/>
        </linearGradient>

        <!-- Career Journey Path Stroke Gradient -->
        <linearGradient id="careerJourneyGrad" x1="30" y1="130" x2="130" y2="30" gradientUnits="userSpaceOnUse">
            <stop offset="0%" stop-color="#356BFA"/>
            <stop offset="40%" stop-color="#21D2CF"/>
            <stop offset="85%" stop-color="#F4C46A"/>
            <stop offset="100%" stop-color="#FFF8E7"/>
        </linearGradient>

        <!-- Open Platform Orbit Ring Gradient -->
        <linearGradient id="platformOrbitGrad" x1="24" y1="80" x2="136" y2="80" gradientUnits="userSpaceOnUse">
            <stop offset="0%" stop-color="#356BFA" stop-opacity="0.2"/>
            <stop offset="50%" stop-color="#21D2CF" stop-opacity="0.6"/>
            <stop offset="100%" stop-color="#F4C46A" stop-opacity="0.3"/>
        </linearGradient>

        <!-- Northstar Radial Radiance -->
        <radialGradient id="starRadiance" cx="50%" cy="50%" r="50%">
            <stop offset="0%" stop-color="#FFFFFF"/>
            <stop offset="30%" stop-color="#F4C46A" stop-opacity="0.9"/>
            <stop offset="70%" stop-color="#21D2CF" stop-opacity="0.4"/>
            <stop offset="100%" stop-color="#21D2CF" stop-opacity="0"/>
        </radialGradient>

        <!-- Glow Filters -->
        <filter id="starGlow" x="-50%" y="-50%" width="200%" height="200%">
            <feGaussianBlur in="SourceGraphic" stdDeviation="2.5" result="blur"/>
            <feMerge>
                <feMergeNode in="blur"/>
                <feMergeNode in="SourceGraphic"/>
            </feMerge>
        </filter>
        <filter id="pathGlow" x="-30%" y="-30%" width="160%" height="160%">
            <feGaussianBlur in="SourceGraphic" stdDeviation="2" result="blur"/>
            <feMerge>
                <feMergeNode in="blur"/>
                <feMergeNode in="SourceGraphic"/>
            </feMerge>
        </filter>
    </defs>

    <!-- Layer 0: Ambient Backdrop Glow -->
    <circle cx="80" cy="80" r="76" fill="url(#northstarAmbientGlow)"/>

    <!-- Layer 1: Base Plate Squircle (Obsidian Shield Base) -->
    <rect x="18" y="18" width="124" height="124" rx="34" fill="url(#northstarPlateGrad)"/>
    <rect x="18" y="18" width="124" height="124" rx="34" stroke="url(#northstarBorderGrad)" stroke-width="1.75" fill="none"/>

    <!-- Layer 2: Surrounding Open Orbital Ring (Connected Platform) -->
    <path d="M 32 94 A 56 36 0 1 1 128 66" fill="none" stroke="url(#platformOrbitGrad)" stroke-width="2" stroke-dasharray="4 3" stroke-linecap="round" opacity="0.75"/>

    <!-- Layer 3: Curved Career Journey Route -->
    <path d="M 38 122 C 48 94, 68 84, 84 76 C 98 68, 110 54, 122 38" fill="none" stroke="#21D2CF" stroke-width="6" stroke-linecap="round" opacity="0.25" filter="url(#pathGlow)"/>
    <path d="M 38 122 C 48 94, 68 84, 84 76 C 98 68, 110 54, 122 38" fill="none" stroke="url(#careerJourneyGrad)" stroke-width="3.5" stroke-linecap="round" filter="url(#pathGlow)"/>

    <!-- Layer 4: Three Milestone Waypoints (Explore, Prepare, Pursue) -->
    <!-- Waypoint 1: Explore (Electric Blue) -->
    <circle cx="44" cy="110" r="7" fill="#356BFA" fill-opacity="0.25"/>
    <circle cx="44" cy="110" r="4" fill="#101D31" stroke="#356BFA" stroke-width="2"/>
    <circle cx="44" cy="110" r="1.8" fill="#21D2CF"/>

    <!-- Waypoint 2: Prepare (Career Cyan) -->
    <circle cx="80" cy="78" r="8" fill="#21D2CF" fill-opacity="0.25"/>
    <circle cx="80" cy="78" r="4.5" fill="#101D31" stroke="#21D2CF" stroke-width="2"/>
    <circle cx="80" cy="78" r="2" fill="#FFFFFF"/>

    <!-- Waypoint 3: Pursue (Warm Northstar Gold) -->
    <circle cx="106" cy="56" r="8.5" fill="#F4C46A" fill-opacity="0.28"/>
    <circle cx="106" cy="56" r="5" fill="#101D31" stroke="#F4C46A" stroke-width="2.2"/>
    <circle cx="106" cy="56" r="2.2" fill="#FFF6E0"/>

    <!-- Layer 5: Destination Northstar (Radiant 4-Pointed Star of Opportunity) -->
    <circle cx="122" cy="38" r="15" fill="url(#starRadiance)" filter="url(#starGlow)"/>
    <path d="M 122 22 L 125.5 34.5 L 138 38 L 125.5 41.5 L 122 54 L 118.5 41.5 L 106 38 L 118.5 34.5 Z" fill="#F4C46A" filter="url(#starGlow)"/>
    <path d="M 122 25 L 124.5 35.5 L 135 38 L 124.5 40.5 L 122 51 L 119.5 40.5 L 109 38 L 119.5 35.5 Z" fill="#FFFFFF"/>
    <circle cx="122" cy="38" r="2" fill="#FFFDF0"/>

    <!-- Micro Star Sparkles along the horizon -->
    <circle cx="66" cy="42" r="1.2" fill="#21D2CF" opacity="0.6"/>
    <circle cx="134" cy="72" r="1.5" fill="#F4C46A" opacity="0.5"/>
    <circle cx="96" cy="118" r="1.2" fill="#356BFA" opacity="0.6"/>
</svg>"""

with open(os.path.join(brand_assets_dir, 'northstar-path.svg'), 'w', encoding='utf-8') as f:
    f.write(svg_content)

with open(os.path.join(static_brand_dir, 'northstar-path.svg'), 'w', encoding='utf-8') as f:
    f.write(svg_content)

print("Saved master SVG files successfully.")

# Now render high-fidelity raster PNGs at multiple resolutions:
# We render at supersampled 1024x1024 with antialiasing and smooth curves, then scale down
W = 1024
H = 1024
scale = W / 160.0

img = Image.new('RGBA', (W, H), (0, 0, 0, 0))
draw = ImageDraw.Draw(img)

# 1. Ambient Glow
glow_img = Image.new('RGBA', (W, H), (0, 0, 0, 0))
glow_draw = ImageDraw.Draw(glow_img)
glow_draw.ellipse([int(4 * scale), int(4 * scale), int(156 * scale), int(156 * scale)], fill=(33, 210, 207, 75))
glow_img = glow_img.filter(ImageFilter.GaussianBlur(int(24 * scale)))
img = Image.alpha_composite(img, glow_img)
draw = ImageDraw.Draw(img)

# 2. Base Squircle Plate
rect_box = [int(18 * scale), int(18 * scale), int(142 * scale), int(142 * scale)]
corner_r = int(34 * scale)
draw.rounded_rectangle(rect_box, radius=corner_r, fill=(16, 29, 49, 255), outline=(33, 210, 207, 230), width=int(2.5 * scale))

# 3. Surrounding Open Orbit Arc
orbit_box = [int(24 * scale), int(44 * scale), int(136 * scale), int(116 * scale)]
draw.arc(orbit_box, start=160, end=380, fill=(33, 210, 207, 160), width=int(2.2 * scale))

# 4. Career Journey Curve (Cubic bezier sampling)
def bezier_point(p0, p1, p2, p3, t):
    x = (1-t)**3 * p0[0] + 3*(1-t)**2 * t * p1[0] + 3*(1-t) * t**2 * p2[0] + t**3 * p3[0]
    y = (1-t)**3 * p0[1] + 3*(1-t)**2 * t * p1[1] + 3*(1-t) * t**2 * p2[1] + t**3 * p3[1]
    return (x, y)

p0 = (38 * scale, 122 * scale)
p1 = (60 * scale, 86 * scale)
p2 = (90 * scale, 74 * scale)
p3 = (122 * scale, 38 * scale)

curve_pts = [bezier_point(p0, p1, p2, p3, i / 200.0) for i in range(201)]

# Draw curve with color transition
for i in range(len(curve_pts) - 1):
    t = i / float(len(curve_pts))
    # Interpolate from Electric Blue (53, 107, 250) -> Cyan (33, 210, 207) -> Gold (244, 196, 106)
    if t < 0.5:
        sub_t = t / 0.5
        r = int(53 + sub_t * (33 - 53))
        g = int(107 + sub_t * (210 - 107))
        b = int(250 + sub_t * (207 - 250))
    else:
        sub_t = (t - 0.5) / 0.5
        r = int(33 + sub_t * (244 - 33))
        g = int(210 + sub_t * (196 - 210))
        b = int(207 + sub_t * (106 - 207))
    draw.line([curve_pts[i], curve_pts[i+1]], fill=(r, g, b, 255), width=int(4.5 * scale))

# 5. Three Waypoints (Explore, Prepare, Pursue)
# W1: Explore (Electric Blue) at (44, 110)
w1 = (int(44 * scale), int(110 * scale))
r1 = int(7 * scale)
draw.ellipse([w1[0]-r1, w1[1]-r1, w1[0]+r1, w1[1]+r1], fill=(53, 107, 250, 80))
draw.ellipse([w1[0]-int(4*scale), w1[1]-int(4*scale), w1[0]+int(4*scale), w1[1]+int(4*scale)], fill=(16, 29, 49, 255), outline=(53, 107, 250, 255), width=int(2*scale))
draw.ellipse([w1[0]-int(1.8*scale), w1[1]-int(1.8*scale), w1[0]+int(1.8*scale), w1[1]+int(1.8*scale)], fill=(33, 210, 207, 255))

# W2: Prepare (Career Cyan) at (80, 78)
w2 = (int(80 * scale), int(78 * scale))
r2 = int(8 * scale)
draw.ellipse([w2[0]-r2, w2[1]-r2, w2[0]+r2, w2[1]+r2], fill=(33, 210, 207, 90))
draw.ellipse([w2[0]-int(4.5*scale), w2[1]-int(4.5*scale), w2[0]+int(4.5*scale), w2[1]+int(4.5*scale)], fill=(16, 29, 49, 255), outline=(33, 210, 207, 255), width=int(2*scale))
draw.ellipse([w2[0]-int(2*scale), w2[1]-int(2*scale), w2[0]+int(2*scale), w2[1]+int(2*scale)], fill=(255, 255, 255, 255))

# W3: Pursue (Warm Northstar Gold) at (106, 56)
w3 = (int(106 * scale), int(56 * scale))
r3 = int(8.5 * scale)
draw.ellipse([w3[0]-r3, w3[1]-r3, w3[0]+r3, w3[1]+r3], fill=(244, 196, 106, 100))
draw.ellipse([w3[0]-int(5*scale), w3[1]-int(5*scale), w3[0]+int(5*scale), w3[1]+int(5*scale)], fill=(16, 29, 49, 255), outline=(244, 196, 106, 255), width=int(2.2*scale))
draw.ellipse([w3[0]-int(2.2*scale), w3[1]-int(2.2*scale), w3[0]+int(2.2*scale), w3[1]+int(2.2*scale)], fill=(255, 246, 224, 255))

# 6. Radiant Northstar at (122, 38)
star_c = (122 * scale, 38 * scale)
# Outer Star Glow
star_glow = Image.new('RGBA', (W, H), (0, 0, 0, 0))
sg_draw = ImageDraw.Draw(star_glow)
sg_draw.ellipse([int(star_c[0] - 18 * scale), int(star_c[1] - 18 * scale), int(star_c[0] + 18 * scale), int(star_c[1] + 18 * scale)], fill=(244, 196, 106, 160))
star_glow = star_glow.filter(ImageFilter.GaussianBlur(int(10 * scale)))
img = Image.alpha_composite(img, star_glow)
draw = ImageDraw.Draw(img)

# 4-Point Diamond Star Polygon
outer_star = [
    (122 * scale, 22 * scale),
    (125.5 * scale, 34.5 * scale),
    (138 * scale, 38 * scale),
    (125.5 * scale, 41.5 * scale),
    (122 * scale, 54 * scale),
    (118.5 * scale, 41.5 * scale),
    (106 * scale, 38 * scale),
    (118.5 * scale, 34.5 * scale)
]
draw.polygon(outer_star, fill=(244, 196, 106, 255))

inner_star = [
    (122 * scale, 26 * scale),
    (124.5 * scale, 35.5 * scale),
    (134 * scale, 38 * scale),
    (124.5 * scale, 40.5 * scale),
    (122 * scale, 50 * scale),
    (119.5 * scale, 40.5 * scale),
    (110 * scale, 38 * scale),
    (119.5 * scale, 35.5 * scale)
]
draw.polygon(inner_star, fill=(255, 255, 255, 255))
draw.ellipse([int(star_c[0] - 2*scale), int(star_c[1] - 2*scale), int(star_c[0] + 2*scale), int(star_c[1] + 2*scale)], fill=(255, 253, 240, 255))

# Export variants
sizes = {
    512: ['northstar-path-512.png', 'icon-512.png'],
    192: ['northstar-path-192.png', 'icon-192.png'],
    180: ['northstar-path-180.png', 'apple-touch-icon.png'],
    48:  ['northstar-path-48.png',  'favicon-48x48.png'],
    32:  ['northstar-path-32.png',  'favicon-32x32.png'],
    16:  ['northstar-path-16.png',  'favicon-16x16.png']
}

ico_images = []

for sz, filenames in sizes.items():
    resized = img.resize((sz, sz), Image.Resampling.LANCZOS)
    for fname in filenames:
        resized.save(os.path.join(brand_assets_dir, fname), format='PNG')
        resized.save(os.path.join(static_brand_dir, fname), format='PNG')
    if sz in [16, 32, 48]:
        ico_images.append(resized)

# Save multi-size favicon.ico (16, 32, 48)
ico_16 = img.resize((16, 16), Image.Resampling.LANCZOS)
ico_32 = img.resize((32, 32), Image.Resampling.LANCZOS)
ico_48 = img.resize((48, 48), Image.Resampling.LANCZOS)

ico_48.save(os.path.join(brand_assets_dir, 'favicon.ico'), format='ICO', sizes=[(16, 16), (32, 32), (48, 48)])
ico_48.save(os.path.join(static_brand_dir, 'favicon.ico'), format='ICO', sizes=[(16, 16), (32, 32), (48, 48)])
ico_48.save(os.path.join(base_dir, 'src', 'main', 'resources', 'static', 'favicon.ico'), format='ICO', sizes=[(16, 16), (32, 32), (48, 48)])

print("Successfully generated all Northstar Path icon variants (16, 32, 48, 180, 192, 512, ICO).")
