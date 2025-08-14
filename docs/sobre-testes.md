# Executando Testes

Este projeto possui dois tipos de testes: de unidade e de integração, que são executados em fases diferentes do ciclo de vida do Maven para otimizar o tempo de build.

### Executar Todos os Testes (Unidade e Integração)

Este é o comando ideal para o fluxo de CI/CD. Ele executará os testes de unidade e, em seguida, os de integração.

```bash
mvn verify
```

### Executar Apenas Testes de Unidade

Use este comando para um ciclo de feedback rápido no desenvolvimento local. Ele irá rodar todas as classes de teste que **não** terminam com o sufixo `IT`.

```bash
mvn test
```

### Executar Apenas Testes de Integração

Use este comando para rodar apenas os testes de integração, o que é útil para quando você está focado em um recurso que depende de recursos externos (como o banco de dados).

```bash
mvn failsafe:integration-test
```