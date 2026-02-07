import bcrypt

# Contraseña a encriptar
password = "admin".encode('utf-8')

# Generar un nuevo hash
hashed = bcrypt.hashpw(password, bcrypt.gensalt())

print("Nuevo hash generado:", hashed.decode('utf-8'))