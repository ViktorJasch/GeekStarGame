#version 120

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float reddish;

void main() {
  gl_FragColor = texture2D(u_texture, v_texCoords);
  if(v_texCoords.y > 0.8) {
    gl_FragColor.r += reddish * (v_texCoords.y - 0.8) * 2;
  }
  if(v_texCoords.y < 0.2) {
    gl_FragColor.r += reddish * (0.2 - v_texCoords.y) * 2;
  }
}
