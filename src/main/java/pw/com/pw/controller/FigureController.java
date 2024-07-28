package pw.com.pw.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import pw.com.pw.domain.Figure;
import pw.com.pw.domain.Usuario;
import pw.com.pw.service.FigureService;
import pw.com.pw.service.FileStorageService;
import pw.com.pw.service.UsuarioService;

@Controller
public class FigureController {
    
    @Autowired
    private final FigureService service;
    @Autowired
    private final FileStorageService fileStorageService;
    @Autowired
    private final UsuarioService usuarioService;
    
    public FigureController(FigureService service, FileStorageService fileStorageService, UsuarioService usuarioService) {
        this.service = service;
        this.fileStorageService = fileStorageService;
        this.usuarioService = null;
    }

    @GetMapping("/index")
    public String index(HttpServletRequest request,HttpServletResponse response, Model model){
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            return "redirect:/loginPage";
        }
        // Adiciona o nome do usuário ao modelo se ele estiver logado
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("username", usuarioLogado.getUsername());
       //List<Figure> figures = figureRepository.findByIsDeletedIsNull();
        model.addAttribute("figures", service.findAll());

        // Formata a data e hora para o cookie
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(new Date());

        // Adiciona um cookie chamado "visita" com a data e hora do acesso
        Cookie visitaCookie = new Cookie("visita", formattedDate);
        visitaCookie.setMaxAge(24 * 60 * 60); // 24 horas em segundos
        response.addCookie(visitaCookie);
        
        return "index"; // Retorna o nome do template Thymeleaf (index.html)
    }

    @GetMapping("/admin")
    public String admin(HttpServletRequest request,HttpServletResponse response, Model model){
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            return "redirect:/loginPage";
        }
        // Adiciona o nome do usuário ao modelo se ele estiver logado
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("username", usuarioLogado.getUsername());
       //List<Figure> figures = figureRepository.findByIsDeletedIsNull();
        model.addAttribute("figures", service.findAll());

        // Formata a data e hora para o cookie
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(new Date());

        // Adiciona um cookie chamado "visita" com a data e hora do acesso
        Cookie visitaCookie = new Cookie("visita", formattedDate);
        visitaCookie.setMaxAge(24 * 60 * 60); // 24 horas em segundos
        response.addCookie(visitaCookie);
        
        return "admin"; // Retorna o nome do template Thymeleaf (admin.html)
    }

    @GetMapping("/loginPage")
    public String login(){
        return "loginPage";
    }

    @GetMapping("/CadastroPage")
    public String cadastroPage(){
        return "cadastroUsuario";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        Figure f = new Figure();
        model.addAttribute("figure", f);
        return "cadastrar";
    }

    @GetMapping("/adicionarCarrinho")
    public String adicionarCarrinho(@RequestParam String id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(true); // Obtém ou cria uma nova sessão
        @SuppressWarnings("unchecked")
        List<Figure> carrinho = (List<Figure>) session.getAttribute("carrinho");

        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        model.addAttribute("carrinho", carrinho);

        Optional<Figure> figureOpt = service.findById(id);
        if (figureOpt.isPresent()) {
            Figure figure = figureOpt.get();
            carrinho.add(figure);
            session.setAttribute("carrinho", carrinho);
            model.addAttribute("message", "Item adicionado ao carrinho com sucesso.");
        } else {
            model.addAttribute("error", "Item não encontrado.");
        }

        return "redirect:/index"; // Redireciona de volta para a página inicial após adicionar ao carrinho
    }

    @GetMapping("/verCarrinho")
    public String verCarrinho(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/loginPage";
        }

        @SuppressWarnings("unchecked")
        List<Figure> carrinho = (List<Figure>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }

        model.addAttribute("carrinho", carrinho);
        return "verCarrinho"; // Retorna o nome do template Thymeleaf (verCarrinho.html)
    }

    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpSession session) {
        // Limpar o carrinho da sessão
        session.removeAttribute("carrinho");

        // Adiciona uma mensagem de sucesso na sessão
        session.setAttribute("mensagemSucesso", "Compra realizada com sucesso!");
        return "redirect:/index";
    }

   @PostMapping("/processLogin")
public String processLogin(@RequestParam String username, @RequestParam String password, HttpServletRequest request, Model model) {
    Optional<Usuario> usuarioOpt = usuarioService.findByUsernameAndPassword(username, password);

    if (usuarioOpt.isPresent()) {
        Usuario usuario = usuarioOpt.get();
        HttpSession session = request.getSession();
        session.setAttribute("usuarioLogado", usuario);

        if (usuario.isAdmin()) {
            return "redirect:/admin";
        } else {
            return "redirect:/index";
        }
    } else {
        model.addAttribute("error", "Usuário ou senha inválidos");
        return "loginPage";
    }
}


    @PostMapping("/processCadastroUsuario")
    public String processCadastro(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) boolean isAdmin, Model model) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setIsAdmin(isAdmin);

        usuarioService.create(usuario);
        return "redirect:/loginPage";
    }

    @PostMapping("/processCadastro")
    public ModelAndView processCadastroPage(@ModelAttribute @Valid Figure f, Errors errors, @RequestParam("file") MultipartFile file){
    
        if (errors.hasErrors()) {
            return new ModelAndView("cadastroPage");
        }
    
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && !originalFilename.isEmpty()) {
                f.setImageUri(originalFilename);
                fileStorageService.save(file);
            } else {
                throw new IllegalArgumentException("Arquivo inválido.");
            }
    
            service.create(f);
            RedirectView redirectView = new RedirectView("/admin");
            ModelAndView modelAndView = new ModelAndView(redirectView);
            modelAndView.addObject("msg", "Cadastro realizado com sucesso");
            modelAndView.addObject("filmes", service.findAll());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("errorPage");
            modelAndView.addObject("msg", "Erro ao realizar cadastro: " + e.getMessage());
            return modelAndView;
        }
    }

    @PostMapping("/delete")
    public ModelAndView deleteFigure(@RequestParam("id") String id) {
        try {
            service.delete(id);
            ModelAndView modelAndView = new ModelAndView("admin");
            modelAndView.addObject("msg", "Item deletado com sucesso");
            modelAndView.addObject("figures", service.findAll());
            return modelAndView;
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("errorPage");
            modelAndView.addObject("msg", "Erro ao deletar item: " + e.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/editar")
    public String editarItem(@RequestParam("id") String id, Model model) {
        Optional<Figure> itemOpt = service.findById(id);
        if (itemOpt.isPresent()) {
            model.addAttribute("Figure", itemOpt.get());
            return "editarFigure";
        } else {
            // Item não encontrado, redireciona para a página de erro ou outra página adequada
            return "redirect:/erro";
        }
    }

    @PostMapping("/processarEdicao")
    public String processarEdicao(Figure figure) {
        service.update(figure);
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/loginPage";
    }
    
}
