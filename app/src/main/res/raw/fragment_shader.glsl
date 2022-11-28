precision highp float;

uniform vec4 u_BaseColor;

uniform float u_FrequencyFactor;
uniform float u_AmplitudeFactor;

uniform float u_RealFrequencyFactor;
uniform float u_RealAmplitudeFactor;

uniform float u_TimeSinceCorrect;

uniform float u_Time;
uniform vec2 u_Resolution;

float plot(vec2 uv, float pct) {
    float thickness = 0.02; // 0.02 is ok
    return smoothstep(pct - thickness, pct, uv.y) -
    smoothstep(pct, pct + thickness, uv.y);
}

void main() {
    // Surface position
    vec2 uv = ( gl_FragCoord.xy - 0.5*u_Resolution.xy ) / u_Resolution.y;
    // Variables
    float velocity = 3.0;
    float frequency = 5.0 + 10.0 * u_FrequencyFactor;
    float amplitude = 0.1 + 0.40 * u_AmplitudeFactor;
    vec3 green = vec3(0.0, 1.0, 0.0);
    vec3 white = vec3(1.0);
    vec3 dkGreen = vec3(0.0, 0.3, 0.0);
    vec3 red = vec3(1.0, 0.0, 0.0);
    vec3 col = vec3(0.0);
    // Real
    float realFrequency = 5.0 + 10.0 * u_RealFrequencyFactor;
    float realAmplitude = 0.1 + 0.40 * u_RealAmplitudeFactor;
    // GREEN ======================
    // Sin wave to plot
    float gs = sin(uv.x * frequency + u_Time * velocity) * amplitude;
    float gpct = plot(uv, gs);
    if (u_TimeSinceCorrect > 0.05) {
        col += gpct * green;
    } else {
        col += gpct * white;
    }
    // RED ========================
    float rs = sin(uv.x * realFrequency + u_Time * velocity) * realAmplitude;
    float rpct = plot(uv, rs);
    col += rpct * red;
    // Output to screen
    col += dkGreen * smoothstep(0.0, 2.0, u_TimeSinceCorrect * (2.0 - uv.x));
    gl_FragColor = vec4(col, 1.0);
}