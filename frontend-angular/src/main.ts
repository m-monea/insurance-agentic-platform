import 'zone.js';
import { bootstrapApplication } from '@angular/platform-browser';
import { Component, computed, signal } from '@angular/core';
import { HttpClient, provideHttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <main class="page">
    <section class="hero">
      <div>
        <p class="eyebrow">AI Insurance Platform</p>
        <h1>Insurance Claims AI Assistant</h1>
        <p class="subtitle">
          Gestione clienti, sinistri, storico AI e analisi locale gratuita con Ollama.
        </p>
      </div>
      <div class="status-panel"><span class="status-dot"></span>Sistema attivo</div>
    </section>

    <section class="stats">
      <div class="stat"><span>Clienti</span><strong>{{ clienti().length }}</strong></div>
      <div class="stat"><span>Sinistri</span><strong>{{ sinistri().length }}</strong></div>
      <div class="stat"><span>Analisi AI</span><strong>{{ storico().length }}</strong></div>
    </section>

    <section class="card">
      <div class="section-title">
        <h2>Dashboard statistiche</h2>
        <button class="secondary" (click)="caricaTutto()">Aggiorna dashboard</button>
      </div>

      <div class="dashboard-grid">
        <div class="mini-stat"><span>Sinistri AUTO</span><strong>{{ statistiche().auto }}</strong></div>
        <div class="mini-stat"><span>Sinistri CASA</span><strong>{{ statistiche().casa }}</strong></div>
        <div class="mini-stat"><span>Sinistri SALUTE</span><strong>{{ statistiche().salute }}</strong></div>
        <div class="mini-stat"><span>Priorità alta</span><strong>{{ statistiche().alta }}</strong></div>
        <div class="mini-stat"><span>Priorità media</span><strong>{{ statistiche().media }}</strong></div>
        <div class="mini-stat"><span>Priorità bassa</span><strong>{{ statistiche().bassa }}</strong></div>
        <div class="mini-stat wide">
          <span>Importo totale stimato</span>
          <strong>{{ statistiche().totaleImporti | number:'1.0-2' }} €</strong>
        </div>
        <div class="mini-stat wide">
          <span>Importo medio stimato</span>
          <strong>{{ statistiche().importoMedio | number:'1.0-2' }} €</strong>
        </div>
      </div>
    </section>

    <section class="grid">
      <div class="card">
        <h2>Nuovo sinistro</h2>

        <label>ID cliente</label>
        <input type="number" [(ngModel)]="sinistro.clienteId">

        <label>Tipo</label>
        <select [(ngModel)]="sinistro.tipo">
          <option>AUTO</option>
          <option>CASA</option>
          <option>SALUTE</option>
        </select>

        <label>Descrizione</label>
        <textarea rows="5" [(ngModel)]="sinistro.descrizione"></textarea>

        <label>Documenti presenti</label>
        <input [(ngModel)]="sinistro.documentiPresenti">

        <label>Importo stimato</label>
        <input type="number" [(ngModel)]="sinistro.importoStimato">

        <button class="primary" (click)="creaSinistro()">Salva sinistro</button>
      </div>

      <div class="card">
        <h2>Gestione sinistri</h2>

        <label>Filtra per ID cliente</label>
        <input type="number" [(ngModel)]="filtroClienteId">

        <button class="secondary" (click)="caricaSinistri()">Carica sinistri</button>
        <button class="danger" (click)="eliminaTuttiSinistri()" *ngIf="sinistri().length">Elimina tutti</button>

        <div class="client-form" *ngIf="sinistroInModifica">
          <h3>Modifica sinistro #{{ sinistroInModifica.id }}</h3>

          <label>ID cliente</label>
          <input type="number" [(ngModel)]="sinistroInModifica.clienteId">

          <label>Tipo</label>
          <select [(ngModel)]="sinistroInModifica.tipo">
            <option>AUTO</option>
            <option>CASA</option>
            <option>SALUTE</option>
          </select>

          <label>Descrizione</label>
          <textarea rows="4" [(ngModel)]="sinistroInModifica.descrizione"></textarea>

          <label>Documenti presenti</label>
          <input [(ngModel)]="sinistroInModifica.documentiPresenti">

          <label>Importo stimato</label>
          <input type="number" [(ngModel)]="sinistroInModifica.importoStimato">

          <button class="primary" (click)="salvaModificaSinistro()">Salva modifiche</button>
          <button class="secondary" (click)="annullaModificaSinistro()">Annulla</button>
        </div>

        <div class="list" *ngIf="sinistri().length; else nessunSinistro">
          <div class="list-item" *ngFor="let s of sinistri()">
            <div class="row">
              <strong>#{{ s.id }} - {{ s.tipo }} - Cliente {{ s.clienteId }}</strong>
              <div>
                <button class="secondary" (click)="preparaModificaSinistro(s)">Modifica</button>
                <button class="primary" (click)="analizzaDaSinistro(s.id)">Analizza</button>
                <button class="danger" (click)="eliminaSinistro(s.id)">Elimina</button>
              </div>
            </div>
            <span>{{ s.descrizione }}</span>
            <small>Documenti: {{ s.documentiPresenti }}</small>
            <small>Importo: {{ s.importoStimato }} €</small>
          </div>
        </div>

        <ng-template #nessunSinistro>
          <p class="muted">Nessun sinistro caricato.</p>
        </ng-template>
      </div>
    </section>

    <section class="grid">
      <div class="card">
        <div class="section-title">
          <h2>Clienti</h2>
          <button class="danger" (click)="eliminaTuttiClienti()" *ngIf="clienti().length">Elimina tutti</button>
        </div>

        <label>Ricerca per cognome</label>
        <input [(ngModel)]="ricercaCognome" placeholder="Es. Rossi">
        <button class="secondary" (click)="caricaClienti()">Cerca / aggiorna</button>

        <div class="client-form">
          <h3>Aggiungi cliente</h3>
          <label>Nome</label><input [(ngModel)]="nuovoCliente.nome">
          <label>Cognome</label><input [(ngModel)]="nuovoCliente.cognome">
          <label>Email</label><input [(ngModel)]="nuovoCliente.email">
          <label>Telefono</label><input [(ngModel)]="nuovoCliente.telefono">
          <label>Numero polizza</label><input [(ngModel)]="nuovoCliente.numeroPolizza">
          <button class="primary" (click)="creaCliente()">Aggiungi cliente</button>
          <p class="muted" *ngIf="messaggioClienti()">{{ messaggioClienti() }}</p>
        </div>

        <div class="client-form" *ngIf="clienteInModifica">
          <h3>Modifica cliente #{{ clienteInModifica.id }}</h3>
          <label>Nome</label><input [(ngModel)]="clienteInModifica.nome">
          <label>Cognome</label><input [(ngModel)]="clienteInModifica.cognome">
          <label>Email</label><input [(ngModel)]="clienteInModifica.email">
          <label>Telefono</label><input [(ngModel)]="clienteInModifica.telefono">
          <label>Numero polizza</label><input [(ngModel)]="clienteInModifica.numeroPolizza">
          <button class="primary" (click)="salvaModificaCliente()">Salva modifiche</button>
          <button class="secondary" (click)="annullaModificaCliente()">Annulla</button>
        </div>

        <div class="list" *ngIf="clienti().length; else clientiVuoti">
          <div class="list-item" *ngFor="let c of clienti()">
            <div class="row">
              <strong>#{{ c.id }} - {{ c.nome }} {{ c.cognome }}</strong>
              <div>
                <button class="secondary" (click)="preparaModificaCliente(c)">Modifica</button>
                <button class="danger" (click)="eliminaCliente(c.id)">Elimina</button>
              </div>
            </div>
            <span>{{ c.email }}</span>
            <span>{{ c.telefono }}</span>
            <small>Polizza: {{ c.numeroPolizza }}</small>
          </div>
        </div>

        <ng-template #clientiVuoti>
          <p class="muted">Nessun cliente caricato.</p>
        </ng-template>
      </div>

      <div class="card">
        <h2>Agente AI</h2>

        <label>ID sinistro</label>
        <input type="number" [(ngModel)]="sinistroDaAnalizzare">

        <button class="primary" (click)="analizza()">Analizza sinistro</button>
        <button class="secondary" (click)="caricaStorico()">Carica storico AI</button>

        <div class="ai-card" *ngIf="risultato() as r">
          <div class="ai-header">
            <div>
              <p class="eyebrow">Analisi AI</p>
              <h3>{{ r.cliente || 'Cliente non disponibile' }}</h3>
            </div>
            <span class="badge" [ngClass]="classePriorita(r.priorita)">
              {{ r.priorita }}
            </span>
          </div>

          <div class="info-grid">
            <div><span>Sinistro</span><strong>#{{ r.sinistroId }}</strong></div>
            <div><span>Motore</span><strong>{{ r.motoreUsato }}</strong></div>
          </div>

          <h4>Riassunto</h4>
          <p>{{ r.riassunto }}</p>

          <h4>Informazioni mancanti</h4>
          <ul *ngIf="r.informazioniMancanti?.length; else nessunaInfo">
            <li *ngFor="let item of r.informazioniMancanti">{{ item }}</li>
          </ul>
          <ng-template #nessunaInfo><p class="muted">Nessuna informazione critica mancante.</p></ng-template>

          <h4>Documenti suggeriti</h4>
          <ul *ngIf="r.documentiSuggeriti?.length; else nessunDoc">
            <li *ngFor="let item of r.documentiSuggeriti">{{ item }}</li>
          </ul>
          <ng-template #nessunDoc><p class="muted">Documentazione sufficiente.</p></ng-template>

          <h4>Report finale</h4>
          <pre class="report">{{ r.reportFinale }}</pre>
        </div>
      </div>
    </section>

    <section class="card">
      <div class="section-title">
        <h2>Storico analisi AI</h2>
        <button class="danger" (click)="eliminaTuttoLoStorico()" *ngIf="storico().length">Elimina tutto</button>
      </div>

      <button class="secondary" (click)="caricaStorico()">Aggiorna storico</button>

      <div class="list" *ngIf="storico().length; else storicoVuoto">
        <div class="list-item" *ngFor="let s of storico()">
          <div class="row">
            <strong>#{{ s.sinistroId }} - {{ s.cliente }}</strong>
            <span class="badge small" [ngClass]="classePriorita(s.priorita)">
              {{ s.priorita }}
            </span>
          </div>
          <span>{{ s.motoreUsato }}</span>
          <small>{{ s.creataIl }}</small>
          <button class="danger" (click)="eliminaAnalisi(s.id)">Elimina</button>
        </div>
      </div>

      <ng-template #storicoVuoto>
        <p class="muted">Nessuna analisi salvata per ora.</p>
      </ng-template>
    </section>
  </main>
  `
})
class AppComponent {
  clienti = signal<any[]>([]);
  sinistri = signal<any[]>([]);
  risultato = signal<any>(null);
  storico = signal<any[]>([]);
  messaggioClienti = signal('');

  statistiche = computed(() => {
    const sinistri = this.sinistri();
    const storico = this.storico();

    const contaTipo = (tipo: string) =>
      sinistri.filter(s => s.tipo === tipo).length;

    const contaPriorita = (priorita: string) =>
      storico.filter(s => s.priorita === priorita).length;

    const importi = sinistri
      .map(s => Number(s.importoStimato || 0))
      .filter(v => !Number.isNaN(v));

    const totaleImporti = importi.reduce((somma, valore) => somma + valore, 0);
    const importoMedio = importi.length ? totaleImporti / importi.length : 0;

    return {
      auto: contaTipo('AUTO'),
      casa: contaTipo('CASA'),
      salute: contaTipo('SALUTE'),
      alta: contaPriorita('ALTA'),
      media: contaPriorita('MEDIA'),
      bassa: contaPriorita('BASSA'),
      totaleImporti,
      importoMedio
    };
  });

  ricercaCognome = '';
  filtroClienteId: number | null = null;

  nuovoCliente: any = {
    nome: '',
    cognome: '',
    email: '',
    telefono: '',
    numeroPolizza: ''
  };

  clienteInModifica: any = null;
  sinistroInModifica: any = null;

  sinistroDaAnalizzare = 1;

  sinistro: any = {
    clienteId: 1,
    tipo: 'AUTO',
    descrizione: 'Tamponamento con danno al paraurti posteriore.',
    documentiPresenti: 'foto danno, denuncia',
    importoStimato: 1200
  };

  constructor(private http: HttpClient) {
    this.caricaTutto();
  }

  caricaTutto() {
    this.caricaClienti();
    this.caricaSinistri();
    this.caricaStorico();
  }

  caricaClienti() {
    const query = this.ricercaCognome.trim()
      ? '?cognome=' + encodeURIComponent(this.ricercaCognome.trim())
      : '';

    this.http.get<any[]>('/api/clienti' + query)
      .subscribe(v => this.clienti.set(v));
  }

  creaCliente() {
    this.http.post<any>('/api/clienti', this.nuovoCliente)
      .subscribe({
        next: () => {
          this.messaggioClienti.set('Cliente aggiunto correttamente.');
          this.nuovoCliente = { nome: '', cognome: '', email: '', telefono: '', numeroPolizza: '' };
          this.caricaClienti();
        },
        error: () => this.messaggioClienti.set('Errore: controlla i dati del cliente.')
      });
  }

  preparaModificaCliente(cliente: any) {
    this.clienteInModifica = { ...cliente };
  }

  annullaModificaCliente() {
    this.clienteInModifica = null;
  }

  salvaModificaCliente() {
    this.http.put<any>('/api/clienti/' + this.clienteInModifica.id, this.clienteInModifica)
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
    this.http.delete('/api/clienti/' + id).subscribe(() => this.caricaClienti());
  }

  eliminaTuttiClienti() {
    if (!confirm('Vuoi eliminare tutti i clienti?')) return;
    this.http.delete('/api/clienti').subscribe(() => this.clienti.set([]));
  }

  caricaSinistri() {
    const query = this.filtroClienteId ? '?clienteId=' + this.filtroClienteId : '';
    this.http.get<any[]>('/api/sinistri' + query)
      .subscribe(v => this.sinistri.set(v));
  }

  creaSinistro() {
    this.http.post<any>('/api/sinistri', this.sinistro)
      .subscribe(v => {
        this.sinistroDaAnalizzare = v.id;
        this.caricaSinistri();
      });
  }

  preparaModificaSinistro(sinistro: any) {
    this.sinistroInModifica = { ...sinistro };
  }

  annullaModificaSinistro() {
    this.sinistroInModifica = null;
  }

  salvaModificaSinistro() {
    this.http.put<any>('/api/sinistri/' + this.sinistroInModifica.id, this.sinistroInModifica)
      .subscribe(() => {
        this.sinistroInModifica = null;
        this.caricaSinistri();
      });
  }

  eliminaSinistro(id: number) {
    if (!confirm('Vuoi eliminare questo sinistro?')) return;
    this.http.delete('/api/sinistri/' + id).subscribe(() => this.caricaSinistri());
  }

  eliminaTuttiSinistri() {
    if (!confirm('Vuoi eliminare tutti i sinistri?')) return;
    this.http.delete('/api/sinistri').subscribe(() => this.sinistri.set([]));
  }

  analizzaDaSinistro(id: number) {
    this.sinistroDaAnalizzare = id;
    this.analizza();
  }

  analizza() {
    this.http.post<any>('/api/agente/analizza/' + this.sinistroDaAnalizzare, {})
      .subscribe(v => {
        this.risultato.set(v);
        this.caricaStorico();
      });
  }

  caricaStorico() {
    this.http.get<any[]>('/api/agente/storico')
      .subscribe(v => this.storico.set(v));
  }

  eliminaAnalisi(id: number) {
    this.http.delete('/api/agente/storico/' + id)
      .subscribe(() => this.caricaStorico());
  }

  eliminaTuttoLoStorico() {
    if (!confirm('Vuoi eliminare tutto lo storico AI?')) return;
    this.http.delete('/api/agente/storico').subscribe(() => this.storico.set([]));
  }

  classePriorita(priorita: string) {
    if (priorita === 'ALTA') return 'alta';
    if (priorita === 'MEDIA') return 'media';
    return 'bassa';
  }
}

bootstrapApplication(AppComponent, {
  providers: [provideHttpClient()]
}).catch(err => console.error(err));