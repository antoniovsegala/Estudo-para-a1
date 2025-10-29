public class SistemaEstoque {

    public static void main(String[] args) {
        Estoque estoque = new Estoque();

        // 2 threads de venda
        Thread vendedor1 = new Thread(new Venda(estoque), "Vendedor 1");
        Thread vendedor2 = new Thread(new Venda(estoque), "Vendedor 2");

        // 1 thread de reposição
        Thread repositor = new Thread(new Reposicao(estoque), "Repositor");

        vendedor1.start();
        vendedor2.start();
        repositor.start();

        try {
            vendedor1.join();
            vendedor2.join();
            repositor.join();
        } catch (InterruptedException e) {
            System.out.println("Thread principal interrompida.");
        }

        System.out.println("\nSimulação de estoque finalizada.");
    }
}

// Classe Estoque
class Estoque {
    private int qtd = 0; // começa vazio

    // Método synchronized para vender
    public synchronized void vender() {
        while (qtd == 0) {
            try {
                System.out.println(Thread.currentThread().getName() + " aguardando reposição...");
                wait(); // espera até ter produto
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        qtd--;
        System.out.println(Thread.currentThread().getName() + " vendeu 1 item. Estoque atual: " + qtd);
        notifyAll(); // avisa outras threads
    }

    // Método synchronized para repor
    public synchronized void repor() {
        qtd++;
        System.out.println(Thread.currentThread().getName() + " repôs 1 item. Estoque atual: " + qtd);
        notifyAll(); // acorda threads que estão esperando
    }
}

// Thread de venda
class Venda implements Runnable {
    private Estoque estoque;

    public Venda(Estoque estoque) {
        this.estoque = estoque;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) { // cada vendedor tenta vender 5 vezes
            estoque.vender();
            try {
                Thread.sleep(1000); // pausa entre vendas
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// Thread de reposição
class Reposicao implements Runnable {
    private Estoque estoque;

    public Reposicao(Estoque estoque) {
        this.estoque = estoque;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) { // repõe 10 vezes
            estoque.repor();
            try {
                Thread.sleep(1500); // tempo entre reposições
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
