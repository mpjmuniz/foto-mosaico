package app;

import fotomosaico.Candidatos;
import fotomosaico.Operacoes;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * A Classe <code>FotoMosaicoApp</code> gerencia a interação com o 
 * usuário, recebendo os argumentos e retornando o produto final.
 * 
 * <p>
 * Esta classe depende dos objetos <code>String</code>, <code>File</code>,
 * <code>Buffered Image</code> e <code>Candidatos</code>, bem como as classes
 * de manipulaçao de imagens, <code>ImageIO</code>.
 * 
 * @see BufferedImage
 * @see File
 * @see Candidatos
 * @see String
 *  
 * @author Marcelo Muniz
 */

public class FotoMosaicoApp {

	public static void main(String[] args) {
		try {
			//Parte 1: Captação de argumentos
			File entrada = new File(args[0]);
			String caminho = args[1];
			double limiar = Double.parseDouble(args[2]);
			String saida = args[3];

			//Parte 2: Inicialização e preparação
			BufferedImage imagemIn = ImageIO.read(entrada), imagemOut;
			Candidatos cands = Operacoes.gerarCandidatos(caminho, imagemIn);

			//Parte 3: Execução
			imagemOut = Operacoes.gerarFotoMosaico(imagemIn, cands, limiar);

			//Parte 4: Retorno
			ImageIO.write(imagemOut, "png", new File(saida));
			System.out.println("Localização da imagem final: " + saida);

		} catch (ArrayIndexOutOfBoundsException | NullPointerException | IOException e) {
			System.out.println("Execute o programa, com a seguinte sintaxe: \n "
					+ "java -jar FotoMosaioApp “C:\\entrada.png” “C:\\Ladrilhos” limiar “C:\\saida.png”, \n"
					+ "com caminho de arquivo existente para a entrada, uma pasta existente com os ladrilhos, e uma imagem para a saída."
					+ "\n- Problema: " + e.getMessage() + " -");
		}
	}
}
