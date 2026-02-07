import bcrypt

# Hash actualizado para la contraseña 'admin'
hash_almacenado = b"$2b$12$6yuWUTgIQBF3ScatbNLgBO6uRChdEzUxpAPFKkiDSB0fPco9Ox66W"

# Contraseña a verificar
password = "admin".encode('utf-8')

# Verificar si la contraseña coincide con el hash
if bcrypt.checkpw(password, hash_almacenado):
    print("La contraseña coincide con el hash.")
else:
    print("La contraseña no coincide con el hash.")