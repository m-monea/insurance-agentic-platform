  caricaClienti() {
    const query = this.ricercaCognome.trim()
      ? '?cognome=' + encodeURIComponent(this.ricercaCognome.trim())
      : '';

    this.http.get<any[]>(CUSTOMER_API + '/api/clienti' + query)
      .subscribe(v => this.clienti.set(v));
  }

  creaCliente() {
    this.http.post<any>(CUSTOMER_API + '/api/clienti', this.nuovoCliente)
      .subscribe({
        next: () => {
          this.messaggioClienti.set('Cliente aggiunto correttamente.');
          this.nuovoCliente = { nome: '', cognome: '', email: '', telefono: '', numeroPolizza: '' };
          this.caricaClienti();
        },
        error: () => this.messaggioClienti.set('Errore: controlla i dati del cliente.')
      });
  }

  salvaModificaCliente() {
    this.http.put<any>(CUSTOMER_API + '/api/clienti/' + this.clienteInModifica.id, this.clienteInModifica)
      .subscribe({
        next: () => {
          this.messaggioClienti.set('Cliente aggiornato correttamente.');
          this.clienteInModifica = null;
          this.caricaClienti();
        },
        error: () => this.messaggioClienti.set('Errore durante aggiornamento cliente.')
      });
  }

  eliminaCliente(id: number) {
    if (!confirm('Vuoi eliminare questo cliente?')) return;
    this.http.delete(CUSTOMER_API + '/api/clienti/' + id).subscribe(() => this.caricaClienti());
  }

  eliminaTuttiClienti() {
    if (!confirm('Vuoi eliminare tutti i clienti?')) return;
    this.http.delete(CUSTOMER_API + '/api/clienti').subscribe(() => this.clienti.set([]));
  }

  caricaSinistri() {
    const query = this.filtroClienteId ? '?clienteId=' + this.filtroClienteId : '';
    this.http.get<any[]>(CLAIM_API + '/api/sinistri' + query)
      .subscribe(v => this.sinistri.set(v));
  }

  creaSinistro() {
    this.http.post<any>(CLAIM_API + '/api/sinistri', this.sinistro)
      .subscribe(v => {
        this.sinistroDaAnalizzare = v.id;
        this.caricaSinistri();
      });
  }

  salvaModificaSinistro() {
    this.http.put<any>(CLAIM_API + '/api/sinistri/' + this.sinistroInModifica.id, this.sinistroInModifica)
      .subscribe(() => {
        this.sinistroInModifica = null;
        this.caricaSinistri();
      });
  }

  eliminaSinistro(id: number) {
    if (!confirm('Vuoi eliminare questo sinistro?')) return;
    this.http.delete(CLAIM_API + '/api/sinistri/' + id).subscribe(() => this.caricaSinistri());
  }

  eliminaTuttiSinistri() {
    if (!confirm('Vui eliminare tutti i sinistri?')) return;
    this.http.delete(CLAIM_API + '/api/sinistri').subscribe(() => this.sinistri.set([]));
  }

  analizza() {
    this.http.post<any>(AI_API + '/api/agente/analizza/' + this.sinistroDaAnalizzare, {})
      .subscribe(v => {
        this.risultato.set(v);
        this.caricaStorico();
      });
  }

  caricaStorico() {
    this.http.get<any[]>(AI_API + '/api/agente/storico')
      .subscribe(v => this.storico.set(v));
  }

  eliminaAnalisi(id: number) {
    this.http.delete(AI_API + '/api/agente/storico/' + id)
      .subscribe(() => this.caricaStorico());
  }

  eliminaTuttoLoStorico() {
    if (!confirm('Vuoi eliminare tutto lo storico AI?')) return;
    this.http.delete(AI_API + '/api/agente/storico').subscribe(() => this.storico.set([]));
  }