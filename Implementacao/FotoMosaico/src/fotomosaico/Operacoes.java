package fotomosaico;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * A Classe <code>Operações</code> agrupa todos os procedimentos 
 * utilizados na produção do fotomosaico.
 * 
 * <p>
 * Esta classe depende de objetos do tipo <code>Buffered Image</code>,
 * bem como as operações de obtenção e definição de valores RGB, contidos nas 
 * respectivas classes dos mesmos. Também utiliza Listas Lineares Sequenciais 
 * implementadas na classe <code>Candidatos</code>, e a implementação dos
 * ladrilhos individualmente, com o tipo <code>Candidato</code>. Para a 
 * manipulação de arquivos, se usa o tipo <code>File</code>.
 *
 * @see BufferedImage
 * @see File
 * @see Candidatos
 * 
 * @author Marcelo Muniz
 */
public class Operacoes {

	/**
     * Retorna um número em ponto flutuante de precisão dupla
	 * representando a intensidade média da imagem passada em argumento.
     * 
	 * <p>
     *
     * Uma exceção do tipo <code>InvalidImageException</code> pode ser lançada
     * se a referência da <code>Buffered Image</code> passada for nula.
     * 
	 * @param imagem A referência de um objeto do tipo BufferedImage do qual se 
	 *			obterá a intensidade média.
     *          
     * @return  um número em ponto flutuante de precisão dupla representando a 
	 * intensidade média da imagem passada em argumento.
	 * 
     * 
     */
	static double calcularIntensidadeMedia(BufferedImage imagem) {
		if(imagem == null) throw new InvalidImageException("Referência passada inválida.");
		double soma = 0;
					
		for (int i = 0; i < imagem.getWidth(); i++)
			for (int j = 0; j < imagem.getHeight(); j++)
				soma += imagem.getRGB(i, j) & 0xFF;
		
		return soma / (imagem.getWidth() * imagem.getHeight());
	}

	/**
     * Retorna uma imagem composta de outras imagens menores, presentes 
	 * na lista passada como argumento, além da imagem de entrada e um 
	 * limiar representando a precisão.
     * 
	 * <p>
     *
     * Uma exceção do tipo <code>InvalidImageException</code> pode ser lançada
     * se a referência da <code>Buffered Image</code> passada for nula.
	 * Uma exceção do tipo <code>IllegalArgumentException</code> pode ser lançada
     * se o valor de epsilon for menor que 0.
     * 
	 * @param imagem A referência de um objeto do tipo BufferedImage do qual se 
	 *			obterá a intensidade média.
	 * @param cands A lista de Candidatos que irão compor a imagem final.
	 * @param epsilon O valor de precisão para representar a imagem final pelos 
	 *			ladrilhos.
     * 
	 * @exception InvalidImageException se a imagem passada for inválida.
	 * @exception IllegalArgumentException se o valor de epsilon for menor que 0.
     * @return  a imagem final, composta dos ladrilhos.
     * 
     */
	public static BufferedImage gerarFotoMosaico(BufferedImage imagem, Candidatos cands, double epsilon) {
		//Parte 0: Análise de inconsistências
		if(imagem == null) throw new InvalidImageException("Imagem de entrada inválida");
		if(epsilon < 0) throw new IllegalArgumentException("Limiar inválido");
		
		//Parte 1: Inicialização
		BufferedImage output = new BufferedImage(imagem.getWidth() * cands.largura(),
			imagem.getHeight() * cands.altura(), BufferedImage.TYPE_3BYTE_BGR);
		int intensidadeDoPixel;
		//double teste;
		Candidato candAtual;
		int[] rgbCandidato = new int[cands.largura() * cands.altura()];
		
		//Parte 2: Processamento
		for (int i = 0; i < imagem.getWidth(); i++) {
			for (int j = 0; j < imagem.getHeight(); j++) {
				intensidadeDoPixel = imagem.getRGB(i, j)  & 0xFF;
				candAtual = cands.buscaBinaria(intensidadeDoPixel, epsilon);
				
				copiarImagem(candAtual.imagem, output, cands.largura(), cands.altura(), i, j, rgbCandidato);
				candAtual.usos++;
				                
			}
		}
		return output;
	}
	
	/**
     * Copia a imagem da entrada passada para a saída determinada, na 
	 * localização especificada.
     * 
	 * <p>
     * 
	 * @param in A imagem que será copiada.
	 * @param out A imagem que receberá a cópia da entrada.
	 * @param largura largura da imagem que será copiada.
	 * @param altura altura da imagem que será copiada.
	 * @param desLargura deslocamento na largura da imagem que receberá a cópia.
     * @param desAltura deslocamento na altura da imagem que receberá a cópia.
	 * @param container valores da imagem que será copiada.
     */
	private static void copiarImagem(BufferedImage in, BufferedImage out, int largura, int altura, int desLargura, int desAltura, int[] container) {
		in.getRGB(0, 0, largura, altura, container, 0, largura);
		out.setRGB(desLargura * largura,	desAltura * altura, largura, altura, container, 0, largura);
	}
	
	/**
     * Retorna uma lista de <code>Candidatos</code> que irão compor a 
	 * imagem final, a partir da localização passada como argumento.
     * 
	 * <p>
     *
     * Uma exceção do tipo <code>IOException</code> pode ser lançada
     * se o caminho passado for inválido.
	 *  
	 * @param caminho Localização da pasta com as imagens menores que foramarão 
	 * os ladrilhos.
	 * @param ref A imagem de referência para se calcular uma quantidade 
	 * suficiente de ladrilhos.
     * 
	 * @exception IOException se a imagem passada for inválida.
     * 
	 * @return uma lista de <code>Candidatos</code> que irão compor a imagem 
	 * final.
     */
	public static Candidatos gerarCandidatos(String caminho, BufferedImage ref) throws IOException {
		//Parte 1: Interpretar caminho
		File lugar = new File(caminho);
		File[] ladrilhos = lugar.listFiles();

		//Parte 2: Gerar modelo de ladrilho
		Candidato modelo = new Candidato(ImageIO.read(ladrilhos[0]));
		Candidatos output = new Candidatos(modelo);

		//Parte 3: preencher com outros ladrilhos
		int qtd = (int) (0.15 * (ref.getWidth() * ref.getHeight())); 
		// sendo utilizado no máximo 10 vezes, precisamos de 10 % do tamanho da imagem. 
		// 15% para aproximar o valor por cima, evitando problemas.
		
		
		for (int i = 1; i < qtd; i++) output.inserir(new Candidato(ImageIO.read(ladrilhos[i])), i);
		
		output.ordenar();
		
		return output;
	}
}