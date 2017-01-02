module.exports = function (grunt) {
    require('jit-grunt')(grunt);

    grunt.loadNpmTasks('grunt-contrib-jshint');
 
    grunt.initConfig({
        less: {
            development: {
                options: {
                    compress: true,
                    yuicompress: true,
                    optimization: 2
                },
                files: {
                    'src/main/webapp/static/web/css/theme.css': 'src/main/resources/less/theme.less' // destination file and source file
                }
            }
        },
        jshint: {
            files: [
                'GruntFile.js',
                'src/main/resources/javascript/**/*.js',
                'src/test/javascript/**/*.js'
            ],
            options: {
                ignores: [
                    'src/main/resources/javascript/lib/**/*.js'
                ]
            }
        }
    });
    grunt.registerTask('default', ['less', 'jshint']);
};
