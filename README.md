# 📱 Controle de Catálogo (Gestão de Restaurantes)

Aplicativo Android nativo desenvolvido para o gerenciamento de cardápios, controle de estoque e categorização de produtos em tempo real para microempresas do setor alimentício. Projeto desenvolvido com arquitetura robusta durante o curso de Engenharia da Computação (UTFPR).

## ✨ Funcionalidades e Requisitos Técnicos Aplicados
- **Banco de Dados Local (Room):** Arquitetura com múltiplas entidades (Categorias e Produtos) e relacionamento por Chave Estrangeira (Foreign Key) com ação de cascata.
- **CRUD Completo:** Inserção, leitura, atualização e exclusão de dados integrados a *Adapters* customizados.
- **Componentes Nativos Avançados:** Manipulação de datas (`LocalDate`) via `DatePickerDialog` integrado ao SQLite via TypeConverters, e validações de segurança com `AlertDialog`.
- **Menus Interativos:** Implementação de Menu de Opções superior e Menu de Ação Contextual (seleção longa para exclusão em massa).
- **Armazenamento de Preferências:** Salvamento contínuo das configurações escolhidas pelo usuário utilizando `SharedPreferences`.
- **Internacionalização:** Suporte a múltiplos idiomas (Inglês como padrão global e Português do Brasil).

## 🛠️ Tecnologias Utilizadas
- **Linguagem:** Java
- **Ambiente:** Android Studio
- **Persistência de Dados:** Room Database, SharedPreferences
- **Interface:** XML (Views Nativas) com suporte a Edge-to-Edge.

## 📸 Interface e Fluxo de Telas

> **Nota:** As telas abaixo demonstram o fluxo de uso, desde a listagem principal até o cadastro validado de novos itens.
<div align="center">

  <img width="250" alt="tela_controle_de_catalogo" src="https://github.com/user-attachments/assets/9a8982e3-28c4-433c-b517-652538cd2516" />
  <br><br>

  <img width="250" alt="tela_cadastro_de_produto" src="https://github.com/user-attachments/assets/fc7d1d7e-a17a-4e76-b67f-d4d15aae7745" />
  <br><br>

  <img width="250" alt="tela_lista_de_categorias" src="https://github.com/user-attachments/assets/9227407b-bfdf-4de8-87cb-ab4fd429b17b" />
  <br><br>

  <img width="250" alt="tela_configuracoes" src="https://github.com/user-attachments/assets/fb71794f-77e6-41b1-8a86-4fa3daf7500c" />
  <br><br>

  <img width="250" alt="tela_sobre" src="https://github.com/user-attachments/assets/a1946e71-8eb1-49c0-9673-82f3d3220643" />

</div>

## 👨‍💻 Autoria
Desenvolvido por **Luan Tarumoto de Macedo**  
*Graduando em Engenharia da Computação - Universidade Tecnológica Federal do Paraná (UTFPR)*
