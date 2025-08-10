Made by:

- **Joel Bastos** (2191618)
- **Ana Martins** (2201789)
- **Tiago Baptista**  (21919826)
- **José Pereira** (2211033)

  

**Plataforma de Monitorização de Embalagens Inteligentes**
  
**Descrição**
Este projeto visa o desenvolvimento de uma aplicação empresarial de monitorização de embalagens inteligentes, que utilizam sensores e conectividade sem fio para acompanhar parâmetros como temperatura, pressão, aceleração e posicionamento global durante o transporte de encomendas. A aplicação será composta por uma interface de backend e frontend, permitindo a comunicação entre os sistemas internos da empresa e a visualização de dados em tempo real.


**Funcionalidades**
  - Monitorização de sensores: Acompanhar dados de sensores como temperatura, aceleração e posicionamento global, integrados em embalagens de produtos.
  - Comunicação com sistemas internos: Interagir com os sistemas de Logística, Gestão Operacional e Apoio ao Cliente da empresa XYZ.
  - Exibição de dados: Gestores podem verificar as últimas leituras de sensores e os clientes podem visualizar o status das suas encomendas, incluindo alertas em caso de impacto ou mudanças significativas nos sensores.
  - Inatividade de sensores após entrega: Os sensores tornam-se inativos quando a encomenda é entregue ao cliente, parando de enviar dados.



**Tecnologias Utilizadas**
  - Backend: Desenvolvido em Jakarta EE com modelo REST.
  - Frontend: Construído com Vue.js/NUXT.
  - Banco de Dados: PostgreSQL.



**Estrutura do Projeto**
  - Backend: Lógica de negócios e acesso a dados via API REST.
  - Frontend: Interface de apresentação para simulação e interação com os sistemas externos e backend.

**Requisitos**
  - A aplicação será auto-contida, não dependendo de sistemas externos.
  - O backend será implementado utilizando o modelo REST Service, com foco na modularidade e boas práticas arquiteturais como MVC e ORM.



**NOTION LINK:**
https://obsidian-tile-511.notion.site/DAE-Project-152be976b0918097a072ca3b38f074c9?pvs=74

**Tecnologias Utilizadas:**
- Docker (Instalar e ativar a tecnologia de virtualização na BIOS)

### Instalar o comando MAKE
1. Abrir a PowerShell em modo administrador
2. Executar: 
```
Set-ExecutionPolicy Bypass -Scope Process -Force;
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072;
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```
3. Fechar a Powershell
4. Abrir novamente a Powershell em modo normal
5. Executar: `choco install make`

### Instalar npm
1. Abrir o Command Prompt
2. Executar:
```
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

# Download and install Node.js:
choco install nodejs --version="22.18.0"
# Verify the Node.js version:
node -v # Should print "v22.18.0".
# Verify npm version:
npm -v # Should print "10.9.3".
```
3. Abrir o caminho app/frontend-appmonitor no Command Prompt
4. Executar: `npm install`

### Instalar o Maven
1. Abrir a página https://maven.apache.org/download.cgi
2. Instalar o zip binário
3. Adicionar o caminho do Maven ao PATH (i.e. *C:\apache-maven-3.9.4*)


## Como Usar:
1. No terminal abrir a pasta app/backend-appmonitor
2. No terminal digitar: `make up` e depois `make deploy`
3. Noutro terminal abrir a pasta app/backend-appmonitor
4. No terminal digitar `npm run start`
5. Abrir o navegador e ir a http://localhost:3000
