import pathlib
import re
root = pathlib.Path('src/main/java/it/unife/sample/backend/controller')
files = sorted(root.glob('*.java'))
for path in files:
    text = path.read_text()
    orig = text
    # Remove imports
    text = re.sub(r'^import it\.unife\.sample\.backend\.exception\.NotFoundException;\s*\n', '', text, flags=re.MULTILINE)
    text = re.sub(r'^import jakarta\.validation\.Valid;\s*\n', '', text, flags=re.MULTILINE)
    text = re.sub(r'@Valid\s+', '', text)
    # Fix UtenteController IDs
    if path.name == 'UtenteController.java':
        text = text.replace('@GetMapping("/{id}")\n    public ResponseEntity<Utente> getById(@PathVariable Long id)', '@GetMapping("/{id}")\n    public ResponseEntity<Utente> getById(@PathVariable String id)')
        text = text.replace('public ResponseEntity<Utente> update(@PathVariable Long id, @RequestBody Utente utente)', 'public ResponseEntity<Utente> update(@PathVariable String id, @RequestBody Utente utente)')
        text = text.replace('public ResponseEntity<Void> delete(@PathVariable Long id)', 'public ResponseEntity<Void> delete(@PathVariable String id)')
    # Replace orElseThrow in CRUD getters
    text = re.sub(r'return service\.findById\(([^)]*)\)\.map\(ResponseEntity::ok\)\.orElseThrow\(\(\) -> new NotFoundException\("[^"]*"\)\);', r'return service.findById(\1).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());', text)
    # Replace throw NotFoundException in update/delete blocks
    text = re.sub(r'if \(!service\.findById\(([^)]*)\)\.isPresent\(\)\) \{\s*\n\s*throw new NotFoundException\("[^"]*"\);\s*\n\s*\}', r'if (!service.findById(\1).isPresent()) {\n            return ResponseEntity.notFound().build();\n        }', text)
    # Ensure ResponseEntity import if needed
    if 'ResponseEntity<' in text and 'import org.springframework.http.ResponseEntity;' not in text:
        if 'import org.springframework.beans.factory.annotation.Autowired;' in text:
            text = text.replace('import org.springframework.beans.factory.annotation.Autowired;\n', 'import org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.http.ResponseEntity;\n')
        else:
            text = text.replace('package it.unife.sample.backend.controller;\n\n', 'package it.unife.sample.backend.controller;\n\nimport org.springframework.http.ResponseEntity;\n\n')
    # Write only if changed
    if text != orig:
        path.write_text(text)
        print('Updated', path.name)
