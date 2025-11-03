package wad;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java wad.Main <caminho/para/o/arquivo.wad>");
            // Usando um valor padrão para facilitar os testes
            System.out.println("Usando o arquivo padrão: doom-data/doom1.wad");
            args = new String[]{"doom-data/doom1.wad"};
        }

        try {
            WadLoader wadLoader = new WadLoader(args[0]);
            wadLoader.load();

            System.out.println("Arquivo WAD carregado com sucesso!");
            System.out.println("Número de lumps: " + wadLoader.getNumLumps());
            System.out.println("Offset do diretório: " + wadLoader.getDirectoryOffset());

            System.out.println("\n--- Primeiros 10 Lumps ---");
            for (int i = 0; i < 10 && i < wadLoader.getLumps().size(); i++) {
                System.out.println(wadLoader.getLumps().get(i));
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo WAD: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
