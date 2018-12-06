package fr.triedge.pixelfx.test;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import fr.triedge.pixelfx.model.Project;
import fr.triedge.pixelfx.model.Tileset;

public class XML {

	public static void main(String[] args) throws JAXBException {
		Project el = new Project();
		el.setName("Triedge");
		Tileset tile1 = new Tileset();
		tile1.setName("Character 1");
		tile1.setImageWidth(256);
		tile1.setImageHeight(256);
		tile1.setImage("SFFODIWRNEWRJETJTO");
		Tileset tile2 = new Tileset();
		tile2.setName("Character 2");
		tile2.setImage("SFFODIWRNEWRJETJTO");
		el.getTilesets().add(tile1);
		el.getTilesets().add(tile2);
		
		File file = new File("storage/Game.pixelfx");
		
		JAXBContext jaxbContext = JAXBContext.newInstance(el.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(el, file);
	}

}
