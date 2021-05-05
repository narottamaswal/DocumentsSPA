package springboot.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@CrossOrigin(origins = "http://localhost:19006", exposedHeaders = {"Content-Disposition"})
public class AppController {
	
	@Autowired
	private DocumentRepo docrepo;
	
	@RequestMapping(value="/a",method=RequestMethod.GET,produces = "application/json")
	public HashMap<String, String> viewhomePage2() {
		 HashMap<String, String> map = new HashMap<>();
		    map.put("key", "value");
		    map.put("foo", "bar");
		    map.put("aa", "bb");
		    return map;
	}
//	@RequestMapping(value="/",method=RequestMethod.GET)
//	public String viewhomePage(Model mv) {
//		List<Document> docslist = docrepo.findByTitle(".pdf");
//		mv.addAttribute("docslist", docslist);
//		return "home";
//	}
//	@PostMapping("/upload")
//	public String uploadFile(@RequestParam("document") MultipartFile file, RedirectAttributes ra) throws IOException {
//		String filename = StringUtils.cleanPath(file.getOriginalFilename());
//		Document doc = new Document();
//		doc.setName(filename);
//		doc.setContent(file.getBytes());
//		doc.setSize(file.getSize());
//		doc.setUploadTime(new Date());
//		docrepo.save(doc);
//		ra.addFlashAttribute("message", "The file is uploaded successfully");
//		return "redirect:/";
//	}
	@GetMapping("/a")
	public void downloadFile(@Param("id") Long id, HttpServletResponse response) throws IOException {
		Optional<Document> doc = docrepo.findById((long) 2);
		if(!doc.isPresent()) {
			try {
				throw new Exception("Could not find doc with id "+id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Document doc2 = doc.get();
		String filename = doc2.getName();
		System.out.print(filename);
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename"+filename;
		response.setHeader(headerKey,headerValue);
		ServletOutputStream os1 = response.getOutputStream();
		os1.write(doc2.getContent());
		os1.close();
	}
//	@GetMapping(value = "/file")
//    public ResponseEntity<ByteArrayResource> getTermsConditions(@Param("id") Long id, HttpServletResponse response) throws FileNotFoundException {
//		Optional<Document> doc = docrepo.findById(id);
//		if(!doc.isPresent()) {
//			try {
//				throw new Exception("Could not find doc with id "+id);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		Document doc2 = doc.get();
//		String filename=doc2.getName();
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_PDF)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
//                .body(new ByteArrayResource(doc2.getContent()));	
//	}
//	@GetMapping(value="/image")
//	@RequestMapping(value="/",method=RequestMethod.GET)
//	public byte[] getImage() {
//		Optional<Document> doc = docrepo.findById((long) 2);
//		if(!doc.isPresent()) {
//			try {
//				throw new Exception("Could not find doc with id ");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		Document doc2 = doc.get();
//		String filename=doc2.getName();
//		return doc2.getContent();
//	}
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ResponseEntity<byte[]> getRandomFile() {
	   System.out.print("respo");
	   Document d = docrepo.findById((long) 2).get();
	   HttpHeaders header = new HttpHeaders();
	   header.setContentType(MediaType.APPLICATION_PDF);
	   header.setContentLength(d.getContent().length);
	   header.set("Content-Disposition", "attachment; filename=" + d.getName());

	   return new ResponseEntity<>(d.getContent(), header, HttpStatus.OK);
	}
	@PostMapping(value="/upload",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public void uploadFile(@RequestParam("document") MultipartFile file) throws IOException {
		System.out.println("upload");
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		Document doc = new Document();
		doc.setName(filename);
		doc.setContent(file.getBytes());
		doc.setSize(file.getSize());
		doc.setUploadTime(new Date());
		docrepo.save(doc);
	}
	@RequestMapping("/download1")
    public ResponseEntity<InputStreamResource> downloadFile1(
            @RequestParam(defaultValue = "E:\\DSL") String fileName) throws IOException {
		System.out.print("downmload");
        File file = new File("E:\\65083220.jpg");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
 
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(MediaType.IMAGE_JPEG)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }
}
